package org.example.samplefortracing.payment.api.dto;

import java.math.BigDecimal;

/**
 * Описывает запрос на проведение оплаты.
 *
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param amount сумма списания
 * @param currency код валюты
 * @param paymentScenario сценарий поведения платёжного сервиса
 */
public record PaymentRequest(
    String orderId,
    String customerId,
    BigDecimal amount,
    String currency,
    String paymentScenario
) {
}
