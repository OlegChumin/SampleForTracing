package org.example.samplefortracing.inventory.api.dto;

/**
 * Описывает результат резервирования товара.
 *
 * @param reservationId идентификатор резерва
 * @param itemId идентификатор товара
 * @param reservedQuantity зарезервированное количество
 * @param status состояние резерва
 */
public record InventoryReservationResponse(
    String reservationId,
    String itemId,
    int reservedQuantity,
    String status
) {
}
