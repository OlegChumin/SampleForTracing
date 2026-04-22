package org.example.samplefortracing.order.api.dto;

import java.math.BigDecimal;

/**
 * Описывает результат полного оформления заказа.
 *
 * @param orderId идентификатор заказа
 * @param orderStatus итоговое состояние заказа
 * @param reservationId идентификатор резерва на складе
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
