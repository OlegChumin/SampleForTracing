package org.example.samplefortracing.order.api.dto;

/**
 * Описывает входной запрос на полное оформление заказа.
 *
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 * @param paymentScenario сценарий поведения платёжного сервиса
 */
public record OrderProcessRequest(String customerId, String itemId, int quantity, String paymentScenario) {
}
