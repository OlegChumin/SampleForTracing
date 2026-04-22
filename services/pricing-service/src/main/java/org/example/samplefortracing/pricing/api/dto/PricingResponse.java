package org.example.samplefortracing.pricing.api.dto;

import java.math.BigDecimal;

/**
 * Описывает рассчитанную стоимость заказа.
 *
 * @param subtotal стоимость без скидки и налога
 * @param discountAmount величина скидки
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
