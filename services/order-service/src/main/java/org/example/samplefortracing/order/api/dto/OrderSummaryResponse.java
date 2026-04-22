package org.example.samplefortracing.order.api.dto;

import java.math.BigDecimal;

/**
 * Описывает сохранённое состояние заказа.
 *
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 * @param status текущее состояние
 * @param totalAmount итоговая сумма
 * @param currency код валюты
 */
public record OrderSummaryResponse(
    String orderId,
    String customerId,
    String itemId,
    int quantity,
    String status,
    BigDecimal totalAmount,
    String currency
) {
}
