package org.example.samplefortracing.inventory;

import org.example.samplefortracing.inventory.api.dto.InventoryReservationRequest;
import org.example.samplefortracing.inventory.api.dto.InventoryReservationResponse;
import org.example.samplefortracing.inventory.service.InventoryReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Проверяет основные сценарии резервирования товарных остатков.
 */
class InventoryReservationServiceTests {

    private final InventoryReservationService inventoryReservationService = new InventoryReservationService();

    /**
     * Проверяет успешное резервирование доступного товара.
     */
    @Test
    void reserveCreatesReservationForAvailableItem() {
        InventoryReservationResponse response = inventoryReservationService.reserve(
            new InventoryReservationRequest("SKU-CHAIR-01", 2)
        );

        assertThat(response.status()).isEqualTo("RESERVED");
        assertThat(response.itemId()).isEqualTo("SKU-CHAIR-01");
        assertThat(response.reservedQuantity()).isEqualTo(2);
    }

    /**
     * Проверяет ошибку при попытке зарезервировать больше доступного количества.
     */
    @Test
    void reserveFailsWhenRequestedQuantityIsTooHigh() {
        assertThatThrownBy(() -> inventoryReservationService.reserve(new InventoryReservationRequest("SKU-DESK-02", 99)))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("409 CONFLICT");
    }
}
