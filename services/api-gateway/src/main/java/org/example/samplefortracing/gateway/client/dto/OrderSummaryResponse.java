package org.example.samplefortracing.gateway.client.dto;

import java.math.BigDecimal;

/**
 * Описывает краткое состояние заказа, возвращаемое сервисом заказов.
 *
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 * @param status текущее состояние заказа
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
