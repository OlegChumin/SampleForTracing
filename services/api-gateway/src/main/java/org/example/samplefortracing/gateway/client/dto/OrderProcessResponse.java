package org.example.samplefortracing.gateway.client.dto;

import java.math.BigDecimal;

/**
 * Описывает ответ сервиса заказов.
 *
 * @param orderId идентификатор заказа
 * @param orderStatus итоговое состояние заказа
 * @param reservationId идентификатор резерва
 * @param paymentId идентификатор платежа
 * @param totalAmount итоговая сумма
 * @param currency код валюты
 */
public record OrderProcessResponse(
    String orderId,
    String orderStatus,
    String reservationId,
    String paymentId,
    BigDecimal totalAmount,
    String currency
) {
}
