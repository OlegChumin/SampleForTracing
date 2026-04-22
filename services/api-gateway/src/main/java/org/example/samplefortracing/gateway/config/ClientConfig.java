package org.example.samplefortracing.gateway.config;

import org.example.samplefortracing.gateway.client.OrderGateway;
import org.example.samplefortracing.gateway.client.RestOrderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Регистрирует REST-клиент для вызова сервиса заказов.
 */
@Configuration
public class ClientConfig {

    /**
     * Создаёт клиент для сервиса заказов.
     *
     * @param builder построитель REST-клиентов
     * @param baseUrl базовый URL сервиса заказов
     * @return клиент сервиса заказов
     */
    @Bean
    public OrderGateway orderGateway(RestClient.Builder builder,
                                     @Value("${client.order-service.base-url}") String baseUrl) {
        return new RestOrderClient(builder.baseUrl(baseUrl).build());
    }
}
