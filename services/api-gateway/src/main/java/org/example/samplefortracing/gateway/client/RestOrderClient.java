package org.example.samplefortracing.gateway.client;

import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * Вызывает сервис заказов через REST.
 */
public class RestOrderClient implements OrderGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestOrderClient.class);

    private final RestClient restClient;

    /**
     * Создаёт REST-клиент для сервиса заказов.
     *
     * @param restClient настроенный REST-клиент
     */
    public RestOrderClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderProcessResponse process(OrderProcessRequest request) {
        LOGGER.info("Calling order-service for item {}", request.itemId());
        return restClient.post()
            .uri("/api/v1/orders/process")
            .body(request)
            .retrieve()
            .body(OrderProcessResponse.class);
    }
}
