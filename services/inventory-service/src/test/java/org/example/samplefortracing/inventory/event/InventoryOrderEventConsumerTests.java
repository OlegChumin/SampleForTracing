package org.example.samplefortracing.inventory.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingScope;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Проверяет consumer событий заказа в сервисе остатков.
 */
class InventoryOrderEventConsumerTests {

    private final KafkaTracingService kafkaTracingService = mock(KafkaTracingService.class);
    private final InventoryOrderEventConsumer consumer = new InventoryOrderEventConsumer(new ObjectMapper(), kafkaTracingService);

    /**
     * Проверяет обработку корректного события создания заказа.
     */
    @Test
    void handleOrderCreatedAcceptsValidPayload() {
        String payload = """
            {"eventId":"EVT-1","orderId":"ORD-1","customerId":"customer-1","itemId":"SKU-1","quantity":2}
            """;
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("checkout.order-created", 0, 0, "ORD-1", payload);
        when(kafkaTracingService.startConsumerSpan(consumerRecord, "kafka checkout.order-created inventory"))
            .thenReturn(mock(KafkaTracingScope.class));

        consumer.handleOrderCreated(consumerRecord);
    }

    /**
     * Проверяет ошибку при невалидном JSON.
     */
    @Test
    void handleOrderCreatedFailsOnInvalidPayload() {
        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("checkout.order-created", 0, 0, "ORD-1", "{broken");
        when(kafkaTracingService.startConsumerSpan(consumerRecord, "kafka checkout.order-created inventory"))
            .thenReturn(mock(KafkaTracingScope.class));

        assertThatThrownBy(() -> consumer.handleOrderCreated(consumerRecord))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid order-created event payload");
    }
}
