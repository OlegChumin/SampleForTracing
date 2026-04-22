package org.example.samplefortracing.order.client.dto;

/**
 * Описывает запрос к сервису расчёта цены.
 *
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 */
public record PricingRequest(String customerId, String itemId, int quantity) {
}
