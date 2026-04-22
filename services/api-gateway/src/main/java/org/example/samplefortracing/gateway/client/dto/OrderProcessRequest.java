package org.example.samplefortracing.gateway.client.dto;

/**
 * Описывает запрос к сервису заказов.
 *
 * @param customerId идентификатор клиента
 * @param itemId идентификатор товара
 * @param quantity количество товара
 * @param paymentScenario сценарий симуляции оплаты
 */
public record OrderProcessRequest(String customerId, String itemId, int quantity, String paymentScenario) {
}
