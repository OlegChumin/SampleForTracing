package org.example.samplefortracing.order;

import org.example.samplefortracing.order.api.dto.OrderProcessRequest;
import org.example.samplefortracing.order.api.dto.OrderProcessResponse;
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
 * Проверяет основной сценарий оркестрации заказа.
 */
class OrderProcessingServiceTests {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @AfterEach
    void tearDown() {
        executorService.shutdown();
    }

    /**
     * Проверяет успешную оркестрацию заказа через downstream сервисы.
     */
    @Test
    void processReturnsCompletedOrder() {
        InventoryGateway inventoryGateway = new StubInventoryGateway();
        PricingGateway pricingGateway = new StubPricingGateway();
        PaymentGateway paymentGateway = new StubPaymentGateway();
        OrderProcessingService orderProcessingService = new OrderProcessingService(
            inventoryGateway,
            pricingGateway,
            paymentGateway,
            new InMemoryOrderRepository(),
            executorService
        );

        OrderProcessResponse response = orderProcessingService.process(
            new OrderProcessRequest("customer-1", "SKU-CHAIR-01", 2, "SUCCESS")
        );

        assertThat(response.orderStatus()).isEqualTo("COMPLETED");
        assertThat(response.reservationId()).isEqualTo("RSV-test");
        assertThat(response.paymentId()).isEqualTo("PAY-test");
        assertThat(response.totalAmount()).isEqualByComparingTo("268.80");
    }

    /**
     * Возвращает тестовый ответ сервиса остатков.
     */
    private static class StubInventoryGateway implements InventoryGateway {

        @Override
        public InventoryReservationResponse reserve(InventoryReservationRequest request) {
            return new InventoryReservationResponse("RSV-test", request.itemId(), request.quantity(), "RESERVED");
        }
    }

    /**
     * Возвращает тестовый ответ сервиса расчёта цены.
     */
    private static class StubPricingGateway implements PricingGateway {

        @Override
        public PricingResponse calculate(PricingRequest request) {
            return new PricingResponse(
                new BigDecimal("240.00"),
                new BigDecimal("0.00"),
                new BigDecimal("28.80"),
                new BigDecimal("268.80"),
                "USD"
            );
        }
    }

    /**
     * Возвращает тестовый ответ платёжного сервиса.
     */
    private static class StubPaymentGateway implements PaymentGateway {

        @Override
        public PaymentResponse charge(PaymentRequest request) {
            return new PaymentResponse("PAY-test", "COMPLETED", "Payment completed");
        }
    }
}
