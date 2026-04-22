package org.example.samplefortracing.order.client.dto;

/**
 * Описывает ответ сервиса резервирования остатков.
 *
 * @param reservationId идентификатор резерва
 * @param itemId идентификатор товара
 * @param reservedQuantity количество в резерве
 * @param status состояние резерва
 */
public record InventoryReservationResponse(String reservationId, String itemId, int reservedQuantity, String status) {
}
