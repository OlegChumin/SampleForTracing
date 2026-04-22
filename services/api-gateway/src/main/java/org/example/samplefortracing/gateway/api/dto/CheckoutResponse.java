package org.example.samplefortracing.gateway.api.dto;

import java.math.BigDecimal;

/**
 * Описывает итоговый ответ API gateway по checkout-сценарию.
 *
 * @param orderId идентификатор заказа
 * @param status итоговое состояние заказа
 * @param reservationId идентификатор резерва
 * @param paymentId идентификатор платежа
 * @param totalAmount итоговая сумма
 * @param currency код валюты
 */
public record CheckoutResponse(
    String orderId,
    String status,
    String reservationId,
    String paymentId,
    BigDecimal totalAmount,
    String currency
) {
}
