package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.PaymentRequest;
import org.example.samplefortracing.order.client.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

/**
 * Вызывает платёжный сервис через REST.
 */
public class RestPaymentClient implements PaymentGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestPaymentClient.class);

    private final RestClient restClient;

    /**
     * Создаёт REST-клиент для платёжного сервиса.
     *
     * @param restClient настроенный REST-клиент
     */
    public RestPaymentClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaymentResponse charge(PaymentRequest request) {
        LOGGER.info("Calling payment-service for order {}", request.orderId());
        return restClient.post()
            .uri("/api/v1/payments/charge")
            .body(request)
            .retrieve()
            .body(PaymentResponse.class);
    }
}
