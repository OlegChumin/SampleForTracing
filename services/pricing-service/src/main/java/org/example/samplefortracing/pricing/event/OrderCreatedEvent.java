package org.example.samplefortracing.pricing.event;

/**
 * Kafka-событие создания заказа.
 *
 * @param eventId идентификатор события
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 */
public record OrderCreatedEvent(String eventId, String orderId, String customerId, String itemId, int quantity) {
}
