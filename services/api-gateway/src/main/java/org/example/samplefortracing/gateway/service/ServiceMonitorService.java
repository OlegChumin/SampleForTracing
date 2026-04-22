package org.example.samplefortracing.gateway.service;

import org.example.samplefortracing.gateway.api.dto.ServiceStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Проверяет доступность сервисов демо-стенда и собирает их статусы.
 */
@Service
public class ServiceMonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceMonitorService.class);

    private final RestClient restClient;
    private final String orderServiceBaseUrl;
    private final String inventoryServiceBaseUrl;
    private final String pricingServiceBaseUrl;
    private final String paymentServiceBaseUrl;

    /**
     * Создаёт сервис мониторинга downstream сервисов.
     *
     * @param builder построитель REST-клиентов
     * @param orderServiceBaseUrl базовый URL сервиса заказов
     * @param inventoryServiceBaseUrl базовый URL сервиса остатков
     * @param pricingServiceBaseUrl базовый URL сервиса расчёта цены
     * @param paymentServiceBaseUrl базовый URL платёжного сервиса
     */
    public ServiceMonitorService(RestClient.Builder builder,
                                 @Value("${client.order-service.base-url}") String orderServiceBaseUrl,
                                 @Value("${client.inventory-service.base-url}") String inventoryServiceBaseUrl,
                                 @Value("${client.pricing-service.base-url}") String pricingServiceBaseUrl,
                                 @Value("${client.payment-service.base-url}") String paymentServiceBaseUrl) {
        this.restClient = builder.build();
        this.orderServiceBaseUrl = orderServiceBaseUrl;
        this.inventoryServiceBaseUrl = inventoryServiceBaseUrl;
        this.pricingServiceBaseUrl = pricingServiceBaseUrl;
        this.paymentServiceBaseUrl = paymentServiceBaseUrl;
    }

    /**
     * Возвращает актуальные статусы локальных сервисов.
     *
     * @return список статусов сервисов
     */
    public List<ServiceStatusResponse> getServiceStatuses() {
        return List.of(
            new ServiceStatusResponse("api-gateway", "http://localhost:8080", "UP"),
            probe("order-service", orderServiceBaseUrl),
            probe("inventory-service", inventoryServiceBaseUrl),
            probe("pricing-service", pricingServiceBaseUrl),
            probe("payment-service", paymentServiceBaseUrl)
        );
    }

    /**
     * Выполняет probe удалённого actuator health endpoint.
     *
     * @param serviceName имя сервиса
     * @param baseUrl базовый URL сервиса
     * @return результат probe
     */
    ServiceStatusResponse probe(String serviceName, String baseUrl) {
        try {
            Map healthResponse = restClient.get()
                .uri(baseUrl + "/actuator/health")
                .retrieve()
                .body(Map.class);
            Object status = healthResponse == null ? "UNKNOWN" : healthResponse.getOrDefault("status", "UNKNOWN");
            return new ServiceStatusResponse(serviceName, baseUrl, String.valueOf(status));
        } catch (RuntimeException exception) {
            LOGGER.warn("Service {} is not reachable on {}", serviceName, baseUrl, exception);
            return new ServiceStatusResponse(serviceName, baseUrl, "DOWN");
        }
    }
}
