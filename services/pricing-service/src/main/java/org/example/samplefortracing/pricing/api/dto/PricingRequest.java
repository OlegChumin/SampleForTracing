package org.example.samplefortracing.pricing.api.dto;

/**
 * Описывает запрос на расчёт цены.
 *
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 */
public record PricingRequest(String customerId, String itemId, int quantity) {
}
