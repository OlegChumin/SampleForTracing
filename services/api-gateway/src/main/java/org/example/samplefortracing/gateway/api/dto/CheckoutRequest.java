package org.example.samplefortracing.gateway.api.dto;

/**
 * Описывает входной запрос на оформление покупки.
 *
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 * @param paymentScenario сценарий поведения платёжного сервиса
 */
public record CheckoutRequest(String customerId, String itemId, int quantity, String paymentScenario) {
}
