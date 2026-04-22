package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.InventoryReservationRequest;
import org.example.samplefortracing.order.client.dto.InventoryReservationResponse;

/**
 * Определяет контракт вызова сервиса резервирования остатков.
 */
public interface InventoryGateway {

    /**
     * Резервирует товар в downstream сервисе.
     *
     * @param request данные о резерве
     * @return результат резервирования
     */
    InventoryReservationResponse reserve(InventoryReservationRequest request);
}
