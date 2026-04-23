package org.example.samplefortracing.order.event;

import java.math.BigDecimal;

/**
 * Событие успешного завершения заказа для Kafka side-chain.
 *
 * @param eventId идентификатор события
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param totalAmount итоговая сумма заказа
 * @param currency валюта заказа
 * @param paymentId идентификатор платежа
 * @param status итоговый статус заказа
 */
public record OrderCompletedEvent(
    String eventId,
    String orderId,
    String customerId,
    BigDecimal totalAmount,
    String currency,
    String paymentId,
    String status
) {
}
