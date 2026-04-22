package org.example.samplefortracing.inventory.api;

import org.example.samplefortracing.inventory.api.dto.InventoryReservationRequest;
import org.example.samplefortracing.inventory.api.dto.InventoryReservationResponse;
import org.example.samplefortracing.inventory.service.InventoryReservationService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет HTTP-контроллер сервиса остатков.
 */
class InventoryControllerTests {

    /**
     * Проверяет делегирование запроса в сервис резервирования.
     */
    @Test
    void reserveDelegatesToService() {
        InventoryController inventoryController = new InventoryController(new InventoryReservationService());

        InventoryReservationResponse response = inventoryController.reserve(new InventoryReservationRequest("SKU-CHAIR-01", 2));

        assertThat(response.itemId()).isEqualTo("SKU-CHAIR-01");
        assertThat(response.status()).isEqualTo("RESERVED");
    }
}
