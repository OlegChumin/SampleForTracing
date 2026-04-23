package org.example.samplefortracing.payment.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Проверяет consumer событий заказа в платёжном сервисе.
 */
class PaymentOrderEventConsumerTests {

    private final PaymentOrderEventConsumer consumer = new PaymentOrderEventConsumer(new ObjectMapper());

    /**
     * Проверяет обработку корректного события завершения заказа.
     */
    @Test
    void handleOrderCompletedAcceptsValidPayload() {
        String payload = """
            {"eventId":"EVT-2","orderId":"ORD-1","customerId":"customer-1","totalAmount":268.80,"currency":"USD","paymentId":"PAY-1","status":"COMPLETED"}
            """;
        consumer.handleOrderCompleted(payload);
    }

    /**
     * Проверяет ошибку при невалидном JSON.
     */
    @Test
    void handleOrderCompletedFailsOnInvalidPayload() {
        assertThatThrownBy(() -> consumer.handleOrderCompleted("{broken"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid order-completed event payload");
    }
}
