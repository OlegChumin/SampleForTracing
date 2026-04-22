package org.example.samplefortracing.order.config;

import org.example.samplefortracing.order.client.InventoryGateway;
import org.example.samplefortracing.order.client.PaymentGateway;
import org.example.samplefortracing.order.client.PricingGateway;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет конфигурацию REST-клиентов сервиса заказов.
 */
class ClientConfigTests {

    /**
     * Проверяет создание клиента сервиса остатков.
     */
    @Test
    void inventoryGatewayCreatesClientBean() {
        ClientConfig clientConfig = new ClientConfig();

        InventoryGateway inventoryGateway = clientConfig.inventoryGateway(RestClient.builder(), "http://localhost:8082");

        assertThat(inventoryGateway).isNotNull();
    }

    /**
     * Проверяет создание клиента сервиса расчёта цены.
     */
    @Test
    void pricingGatewayCreatesClientBean() {
        ClientConfig clientConfig = new ClientConfig();

        PricingGateway pricingGateway = clientConfig.pricingGateway(RestClient.builder(), "http://localhost:8083");

        assertThat(pricingGateway).isNotNull();
    }

    /**
     * Проверяет создание клиента платёжного сервиса.
     */
    @Test
    void paymentGatewayCreatesClientBean() {
        ClientConfig clientConfig = new ClientConfig();

        PaymentGateway paymentGateway = clientConfig.paymentGateway(RestClient.builder(), "http://localhost:8084");

        assertThat(paymentGateway).isNotNull();
    }
}
