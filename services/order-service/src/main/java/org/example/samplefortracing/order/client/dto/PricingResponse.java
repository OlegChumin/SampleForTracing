package org.example.samplefortracing.order.client.dto;

import java.math.BigDecimal;

/**
 * Описывает ответ сервиса расчёта стоимости.
 *
 * @param subtotal стоимость без скидки и налога
 * @param discountAmount сумма скидки
 * @param taxAmount сумма налога
 * @param totalAmount итоговая сумма
 * @param currency код валюты
 */
public record PricingResponse(
    BigDecimal subtotal,
    BigDecimal discountAmount,
    BigDecimal taxAmount,
    BigDecimal totalAmount,
    String currency
) {
}
