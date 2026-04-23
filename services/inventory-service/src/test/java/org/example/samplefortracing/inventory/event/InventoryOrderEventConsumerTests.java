package org.example.samplefortracing.inventory.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Проверяет consumer событий заказа в сервисе остатков.
 */
class InventoryOrderEventConsumerTests {

    private final InventoryOrderEventConsumer consumer = new InventoryOrderEventConsumer(new ObjectMapper());

    /**
     * Проверяет обработку корректного события создания заказа.
     */
    @Test
    void handleOrderCreatedAcceptsValidPayload() {
        String payload = """
            {"eventId":"EVT-1","orderId":"ORD-1","customerId":"customer-1","itemId":"SKU-1","quantity":2}
            """;
        consumer.handleOrderCreated(payload);
    }

    /**
     * Проверяет ошибку при невалидном JSON.
     */
    @Test
    void handleOrderCreatedFailsOnInvalidPayload() {
        assertThatThrownBy(() -> consumer.handleOrderCreated("{broken"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid order-created event payload");
    }
}
