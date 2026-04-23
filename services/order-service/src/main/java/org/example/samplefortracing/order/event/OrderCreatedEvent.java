package org.example.samplefortracing.order.event;

/**
 * Событие создания заказа для Kafka side-chain.
 *
 * @param eventId идентификатор события
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 */
public record OrderCreatedEvent(String eventId, String orderId, String customerId, String itemId, int quantity) {
}
