package org.example.samplefortracing.gateway.config;

import org.example.samplefortracing.gateway.client.OrderGateway;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет конфигурацию REST-клиентов API gateway.
 */
class ClientConfigTests {

    /**
     * Проверяет создание клиента сервиса заказов.
     */
    @Test
    void orderGatewayCreatesClientBean() {
        ClientConfig clientConfig = new ClientConfig();

        OrderGateway orderGateway = clientConfig.orderGateway(RestClient.builder(), "http://localhost:8081");

        assertThat(orderGateway).isNotNull();
    }
}
