package org.example.samplefortracing.order.config;

import org.example.samplefortracing.order.client.InventoryGateway;
import org.example.samplefortracing.order.client.PaymentGateway;
import org.example.samplefortracing.order.client.PricingGateway;
import org.example.samplefortracing.order.client.RestInventoryClient;
import org.example.samplefortracing.order.client.RestPaymentClient;
import org.example.samplefortracing.order.client.RestPricingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Регистрирует REST-клиенты для вызова downstream сервисов.
 */
@Configuration
public class ClientConfig {

    /**
     * Создаёт клиент для сервиса остатков.
     *
     * @param builder построитель REST-клиентов
     * @param baseUrl базовый URL сервиса остатков
     * @return клиент сервиса остатков
     */
    @Bean
    public InventoryGateway inventoryGateway(RestClient.Builder builder,
                                             @Value("${client.inventory-service.base-url}") String baseUrl) {
        return new RestInventoryClient(builder.baseUrl(baseUrl).build());
    }

    /**
     * Создаёт клиент для сервиса расчёта цены.
     *
     * @param builder построитель REST-клиентов
     * @param baseUrl базовый URL сервиса расчёта цены
     * @return клиент сервиса расчёта цены
     */
    @Bean
    public PricingGateway pricingGateway(RestClient.Builder builder,
                                         @Value("${client.pricing-service.base-url}") String baseUrl) {
        return new RestPricingClient(builder.baseUrl(baseUrl).build());
    }

    /**
     * Создаёт клиент для платёжного сервиса.
     *
     * @param builder построитель REST-клиентов
     * @param baseUrl базовый URL платёжного сервиса
     * @return клиент платёжного сервиса
     */
    @Bean
    public PaymentGateway paymentGateway(RestClient.Builder builder,
                                         @Value("${client.payment-service.base-url}") String baseUrl) {
        return new RestPaymentClient(builder.baseUrl(baseUrl).build());
    }
}
