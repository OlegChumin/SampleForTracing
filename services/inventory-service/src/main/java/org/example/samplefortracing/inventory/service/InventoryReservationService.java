package org.example.samplefortracing.inventory.service;

import org.example.samplefortracing.inventory.api.dto.InventoryReservationRequest;
import org.example.samplefortracing.inventory.api.dto.InventoryReservationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

/**
 * Выполняет правила резервирования товарных остатков.
 */
@Service
public class InventoryReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryReservationService.class);

    private static final Map<String, Integer> AVAILABLE_STOCK = Map.of(
        "SKU-CHAIR-01", 12,
        "SKU-DESK-02", 8,
        "SKU-LAMP-03", 25,
        "SKU-MONITOR-04", 6
    );

    /**
     * Резервирует товар, если остатка достаточно.
     *
     * @param request входной запрос на резервирование
     * @return информация о созданном резерве
     */
    public InventoryReservationResponse reserve(InventoryReservationRequest request) {
        int availableUnits = checkAvailability(request.itemId());
        validateRequestedQuantity(request.quantity(), availableUnits);

        String reservationId = "RSV-" + UUID.randomUUID().toString().substring(0, 8);
        LOGGER.info("Inventory reservation {} created for item {}", reservationId, request.itemId());
        return new InventoryReservationResponse(reservationId, request.itemId(), request.quantity(), "RESERVED");
    }

    /**
     * Возвращает доступный остаток по товару.
     *
     * @param itemId идентификатор товара
     * @return доступный остаток
     */
    int checkAvailability(String itemId) {
        Integer availableUnits = AVAILABLE_STOCK.get(itemId);
        if (availableUnits == null) {
            LOGGER.warn("Inventory item {} was not found", itemId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
        LOGGER.info("Inventory availability for item {} is {}", itemId, availableUnits);
        return availableUnits;
    }

    /**
     * Проверяет, что количество для резерва допустимо.
     *
     * @param requestedQuantity запрошенное количество
     * @param availableUnits доступный остаток
     */
    void validateRequestedQuantity(int requestedQuantity, int availableUnits) {
        if (requestedQuantity <= 0) {
            LOGGER.warn("Inventory received non-positive quantity {}", requestedQuantity);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        if (requestedQuantity > availableUnits) {
            LOGGER.warn("Inventory cannot reserve {} units because only {} are available", requestedQuantity, availableUnits);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock");
        }
    }
}
