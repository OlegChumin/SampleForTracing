package org.example.samplefortracing.payment.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingScope;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Проверяет consumer событий заказа в платёжном сервисе.
 */
class PaymentOrderEventConsumerTests {

    private final KafkaTracingService kafkaTracingService = mock(KafkaTracingService.class);
    private final PaymentOrderEventConsumer consumer = new PaymentOrderEventConsumer(new ObjectMapper(), kafkaTracingService);

    /**
     * Проверяет обработку корректного события завершения заказа.
     */
    @Test
    void handleOrderCompletedAcceptsValidPayload() {
        String payload = """
            {"eventId":"EVT-2","orderId":"ORD-1","customerId":"customer-1","totalAmount":268.80,"currency":"USD","paymentId":"PAY-1","status":"COMPLETED"}
            """;
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("checkout.order-completed", 0, 0, "ORD-1", payload);
        when(kafkaTracingService.startConsumerSpan(consumerRecord, "kafka checkout.order-completed payment"))
            .thenReturn(mock(KafkaTracingScope.class));

        consumer.handleOrderCompleted(consumerRecord);
    }

    /**
     * Проверяет ошибку при невалидном JSON.
     */
    @Test
    void handleOrderCompletedFailsOnInvalidPayload() {
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("checkout.order-completed", 0, 0, "ORD-1", "{broken");
        when(kafkaTracingService.startConsumerSpan(consumerRecord, "kafka checkout.order-completed payment"))
            .thenReturn(mock(KafkaTracingScope.class));

        assertThatThrownBy(() -> consumer.handleOrderCompleted(consumerRecord))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid order-completed event payload");
    }
}
