package org.example.samplefortracing.order.api;

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
import org.example.samplefortracing.order.service.OrderProcessingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет HTTP-контроллер сервиса заказов.
 */
class OrderControllerTests {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @AfterEach
    void tearDown() {
        executorService.shutdown();
    }

    /**
     * Проверяет обработку запроса на оформление заказа.
     */
    @Test
    void processDelegatesToService() {
        OrderController orderController = new OrderController(createOrderProcessingService());

        OrderProcessResponse response = orderController.process(new OrderProcessRequest("customer-1", "SKU-1", 1, "SUCCESS"));

        assertThat(response.orderStatus()).isEqualTo("COMPLETED");
    }

    /**
     * Проверяет чтение сохранённого заказа.
     */
    @Test
    void getOrderDelegatesToService() {
        OrderController orderController = new OrderController(createOrderProcessingService());
        OrderProcessResponse processResponse = orderController.process(new OrderProcessRequest("customer-1", "SKU-1", 1, "SUCCESS"));

        OrderSummaryResponse response = orderController.getOrder(processResponse.orderId());

        assertThat(response.status()).isEqualTo("COMPLETED");
    }

    private OrderProcessingService createOrderProcessingService() {
        InventoryGateway inventoryGateway = request -> new InventoryReservationResponse("RSV-1", request.itemId(), request.quantity(), "RESERVED");
        PricingGateway pricingGateway = request -> new PricingResponse(
            new BigDecimal("100.00"),
            BigDecimal.ZERO,
            new BigDecimal("12.00"),
            new BigDecimal("112.00"),
            "USD"
        );
        PaymentGateway paymentGateway = request -> new PaymentResponse("PAY-1", "COMPLETED", "Payment completed");
        return new OrderProcessingService(
            inventoryGateway,
            pricingGateway,
            paymentGateway,
            new InMemoryOrderRepository(),
            executorService
        );
    }
}
