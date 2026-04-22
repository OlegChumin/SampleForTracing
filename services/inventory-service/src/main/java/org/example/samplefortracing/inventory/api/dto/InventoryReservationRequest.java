package org.example.samplefortracing.inventory.api.dto;

/**
 * Описывает запрос на резервирование товара.
 *
 * @param itemId идентификатор товара
 * @param quantity количество единиц товара
 */
public record InventoryReservationRequest(String itemId, int quantity) {
}
