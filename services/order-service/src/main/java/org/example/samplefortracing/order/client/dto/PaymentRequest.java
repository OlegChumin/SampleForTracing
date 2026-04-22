package org.example.samplefortracing.order.client.dto;

import java.math.BigDecimal;

/**
 * Описывает запрос к платёжному сервису.
 *
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param amount сумма оплаты
 * @param currency код валюты
 * @param paymentScenario сценарий симуляции платежа
 */
public record PaymentRequest(
    String orderId,
    String customerId,
    BigDecimal amount,
    String currency,
    String paymentScenario
) {
}
