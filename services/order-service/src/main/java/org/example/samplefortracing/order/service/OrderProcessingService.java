package org.example.samplefortracing.order.service;

import org.example.samplefortracing.order.api.dto.OrderProcessRequest;
import org.example.samplefortracing.order.api.dto.OrderProcessResponse;
import org.example.samplefortracing.order.api.dto.OrderSummaryResponse;
import org.example.samplefortracing.order.client.InventoryGateway;
import org.example.samplefortracing.order.client.PaymentGateway;
import org.example.samplefortracing.order.client.PricingGateway;
import org.example.samplefortracing.order.client.dto.InventoryReservationRequest;
import org.example.samplefortracing.order.client.dto.InventoryReservationResponse;
import org.example.samplefortracing.order.client.dto.PaymentRequest;
import org.example.samplefortracing.order.client.dto.PaymentResponse;
import org.example.samplefortracing.order.client.dto.PricingRequest;
import org.example.samplefortracing.order.client.dto.PricingResponse;
import org.example.samplefortracing.order.repository.InMemoryOrderRepository;
import org.example.samplefortracing.order.service.model.OrderSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;

/**
 * Оркестрирует полный checkout-сценарий заказа.
 */
@Service
public class OrderProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingService.class);

    private final InventoryGateway inventoryGateway;
    private final PricingGateway pricingGateway;
    private final PaymentGateway paymentGateway;
    private final InMemoryOrderRepository inMemoryOrderRepository;
    private final ExecutorService orderTaskExecutor;

    /**
     * Создаёт сервис оркестрации заказа.
     *
     * @param inventoryGateway клиент сервиса остатков
     * @param pricingGateway клиент сервиса расчёта цены
     * @param paymentGateway клиент платёжного сервиса
     * @param inMemoryOrderRepository репозиторий заказов
     * @param orderTaskExecutor пул потоков для параллельных вызовов
     */
    public OrderProcessingService(InventoryGateway inventoryGateway,
                                  PricingGateway pricingGateway,
                                  PaymentGateway paymentGateway,
                                  InMemoryOrderRepository inMemoryOrderRepository,
                                  ExecutorService orderTaskExecutor) {
        this.inventoryGateway = inventoryGateway;
        this.pricingGateway = pricingGateway;
        this.paymentGateway = paymentGateway;
        this.inMemoryOrderRepository = inMemoryOrderRepository;
        this.orderTaskExecutor = orderTaskExecutor;
    }

    /**
     * Выполняет полный сценарий оформления заказа.
     *
     * @param request параметры оформления
     * @return итоговый ответ по заказу
     */
    public OrderProcessResponse process(OrderProcessRequest request) {
        String orderId = createOrderId();
        LOGGER.info("Order {} created and moved to PROCESSING", orderId);

        CompletableFuture<InventoryReservationResponse> inventoryFuture = CompletableFuture.supplyAsync(
            () -> inventoryGateway.reserve(new InventoryReservationRequest(request.itemId(), request.quantity())),
            orderTaskExecutor
        );
        CompletableFuture<PricingResponse> pricingFuture = CompletableFuture.supplyAsync(
            () -> pricingGateway.calculate(new PricingRequest(request.customerId(), request.itemId(), request.quantity())),
            orderTaskExecutor
        );

        InventoryReservationResponse inventoryReservationResponse = joinStage(inventoryFuture, "inventory-service");
        PricingResponse pricingResponse = joinStage(pricingFuture, "pricing-service");
        PaymentResponse paymentResponse = paymentGateway.charge(
            new PaymentRequest(
                orderId,
                request.customerId(),
                pricingResponse.totalAmount(),
                pricingResponse.currency(),
                request.paymentScenario()
            )
        );

        OrderSnapshot orderSnapshot = new OrderSnapshot(
            orderId,
            request.customerId(),
            request.itemId(),
            request.quantity(),
            "COMPLETED",
            pricingResponse.totalAmount(),
            pricingResponse.currency()
        );
        inMemoryOrderRepository.save(orderSnapshot);
        LOGGER.info("Order {} completed successfully", orderId);

        return new OrderProcessResponse(
            orderId,
            orderSnapshot.status(),
            inventoryReservationResponse.reservationId(),
            paymentResponse.paymentId(),
            orderSnapshot.totalAmount(),
            orderSnapshot.currency()
        );
    }

    /**
     * Возвращает сохранённое состояние заказа.
     *
     * @param orderId идентификатор заказа
     * @return найденный заказ
     */
    public OrderSummaryResponse getOrder(String orderId) {
        OrderSnapshot orderSnapshot = inMemoryOrderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        return new OrderSummaryResponse(
            orderSnapshot.orderId(),
            orderSnapshot.customerId(),
            orderSnapshot.itemId(),
            orderSnapshot.quantity(),
            orderSnapshot.status(),
            orderSnapshot.totalAmount(),
            orderSnapshot.currency()
        );
    }

    /**
     * Ожидает завершения асинхронного вызова downstream сервиса.
     *
     * @param future асинхронный результат
     * @param downstreamServiceName имя downstream сервиса
     * @return результат downstream вызова
     * @param <T> тип возвращаемого значения
     */
    <T> T joinStage(CompletableFuture<T> future, String downstreamServiceName) {
        try {
            return future.join();
        } catch (CompletionException completionException) {
            LOGGER.error("Downstream call to {} failed", downstreamServiceName, completionException);
            Throwable cause = completionException.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Downstream call failed");
        }
    }

    /**
     * Формирует идентификатор заказа.
     *
     * @return новый идентификатор заказа
     */
    String createOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
