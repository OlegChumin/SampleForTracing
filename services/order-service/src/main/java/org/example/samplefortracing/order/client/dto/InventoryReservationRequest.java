package org.example.samplefortracing.order.client.dto;

/**
 * Описывает запрос к сервису резервирования остатков.
 *
 * @param itemId идентификатор товара
 * @param quantity количество товара
 */
public record InventoryReservationRequest(String itemId, int quantity) {
}
