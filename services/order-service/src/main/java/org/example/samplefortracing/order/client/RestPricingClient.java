package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.PricingRequest;
import org.example.samplefortracing.order.client.dto.PricingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * Вызывает сервис расчёта цены через REST.
 */
public class RestPricingClient implements PricingGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestPricingClient.class);

    private final RestClient restClient;

    /**
     * Создаёт REST-клиент для сервиса расчёта цены.
     *
     * @param restClient настроенный REST-клиент
     */
    public RestPricingClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PricingResponse calculate(PricingRequest request) {
        LOGGER.info("Calling pricing-service for customer {}", request.customerId());
        return restClient.post()
            .uri("/api/v1/pricing/calculate")
            .body(request)
            .retrieve()
            .body(PricingResponse.class);
    }
}
