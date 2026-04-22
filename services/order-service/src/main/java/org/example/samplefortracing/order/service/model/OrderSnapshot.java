package org.example.samplefortracing.order.service.model;

import java.math.BigDecimal;

/**
 * Описывает внутреннее сохранённое состояние заказа.
 *
 * @param orderId идентификатор заказа
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 * @param status состояние заказа
 * @param totalAmount итоговая сумма
 * @param currency код валюты
 */
public record OrderSnapshot(
    String orderId,
    String customerId,
    String itemId,
    int quantity,
    String status,
    BigDecimal totalAmount,
    String currency
) {
}
