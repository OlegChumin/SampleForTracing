package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.InventoryReservationRequest;
import org.example.samplefortracing.order.client.dto.InventoryReservationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * Вызывает сервис остатков через REST.
 */
public class RestInventoryClient implements InventoryGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestInventoryClient.class);

    private final RestClient restClient;

    /**
     * Создаёт REST-клиент для сервиса остатков.
     *
     * @param restClient настроенный REST-клиент
     */
    public RestInventoryClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryReservationResponse reserve(InventoryReservationRequest request) {
        LOGGER.info("Calling inventory-service for item {}", request.itemId());
        return restClient.post()
            .uri("/api/v1/inventory/reservations")
            .body(request)
            .retrieve()
            .body(InventoryReservationResponse.class);
    }
}
