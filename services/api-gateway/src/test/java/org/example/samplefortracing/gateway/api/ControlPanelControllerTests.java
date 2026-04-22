package org.example.samplefortracing.gateway.api;

import org.example.samplefortracing.gateway.api.dto.CheckoutRequest;
import org.example.samplefortracing.gateway.api.dto.CheckoutResponse;
import org.example.samplefortracing.gateway.api.dto.ServiceStatusResponse;
import org.example.samplefortracing.gateway.client.OrderGateway;
import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.example.samplefortracing.gateway.client.dto.OrderSummaryResponse;
import org.example.samplefortracing.gateway.service.CheckoutGatewayService;
import org.example.samplefortracing.gateway.service.ServiceMonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет API панели управления gateway.
 */
class ControlPanelControllerTests {

    /**
     * Проверяет возврат статусов сервисов.
     */
    @Test
    void getServicesReturnsServiceStatuses() {
        ControlPanelController controlPanelController = new ControlPanelController(createCheckoutGatewayService(), createServiceMonitorService());

        List<ServiceStatusResponse> responses = controlPanelController.getServices();

        assertThat(responses).hasSize(5);
        assertThat(responses.getFirst().serviceName()).isEqualTo("api-gateway");
    }

    /**
     * Проверяет проксирование чтения заказа.
     */
    @Test
    void getOrderReturnsOrderSummary() {
        ControlPanelController controlPanelController = new ControlPanelController(createCheckoutGatewayService(), createServiceMonitorService());

        OrderSummaryResponse response = controlPanelController.getOrder("ORD-1");

        assertThat(response.orderId()).isEqualTo("ORD-1");
    }

    private CheckoutGatewayService createCheckoutGatewayService() {
        OrderGateway orderGateway = new OrderGateway() {
            @Override
            public OrderProcessResponse process(OrderProcessRequest request) {
                return new OrderProcessResponse("ORD-1", "COMPLETED", "RSV-1", "PAY-1", new BigDecimal("112.00"), "USD");
            }

            @Override
            public OrderSummaryResponse getOrder(String orderId) {
                return new OrderSummaryResponse(orderId, "customer-1", "SKU-1", 1, "COMPLETED", new BigDecimal("112.00"), "USD");
            }
        };
        return new CheckoutGatewayService(orderGateway);
    }

    private ServiceMonitorService createServiceMonitorService() {
        return new ServiceMonitorService(RestClient.builder(), "http://localhost:8081", "http://localhost:8082", "http://localhost:8083", "http://localhost:8084") {
            @Override
            public List<ServiceStatusResponse> getServiceStatuses() {
                return List.of(
                    new ServiceStatusResponse("api-gateway", "http://localhost:8080", "UP"),
                    new ServiceStatusResponse("order-service", "http://localhost:8081", "UP"),
                    new ServiceStatusResponse("inventory-service", "http://localhost:8082", "UP"),
                    new ServiceStatusResponse("pricing-service", "http://localhost:8083", "UP"),
                    new ServiceStatusResponse("payment-service", "http://localhost:8084", "UP")
                );
            }
        };
    }
}
