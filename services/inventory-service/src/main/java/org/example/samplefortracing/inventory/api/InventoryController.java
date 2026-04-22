package org.example.samplefortracing.inventory.api;

import org.example.samplefortracing.inventory.api.dto.InventoryReservationRequest;
import org.example.samplefortracing.inventory.api.dto.InventoryReservationResponse;
import org.example.samplefortracing.inventory.service.InventoryReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет HTTP API для резервирования остатков.
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryReservationService inventoryReservationService;

    /**
     * Создаёт контроллер резервирования остатков.
     *
     * @param inventoryReservationService сервис бизнес-логики резервирования
     */
    public InventoryController(InventoryReservationService inventoryReservationService) {
        this.inventoryReservationService = inventoryReservationService;
    }

    /**
     * Резервирует товар для оформления заказа.
     *
     * @param request данные о товаре и количестве
     * @return результат резервирования
     */
    @PostMapping("/reservations")
    public InventoryReservationResponse reserve(@RequestBody InventoryReservationRequest request) {
        LOGGER.info("Inventory reservation requested for item {} with quantity {}", request.itemId(), request.quantity());
        return inventoryReservationService.reserve(request);
    }
}
