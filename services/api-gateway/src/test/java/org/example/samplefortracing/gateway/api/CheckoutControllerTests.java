package org.example.samplefortracing.gateway.api;

import org.example.samplefortracing.gateway.api.dto.CheckoutRequest;
import org.example.samplefortracing.gateway.api.dto.CheckoutResponse;
import org.example.samplefortracing.gateway.client.OrderGateway;
import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.example.samplefortracing.gateway.service.CheckoutGatewayService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет HTTP-контроллер checkout API.
 */
class CheckoutControllerTests {

    /**
     * Проверяет делегирование checkout-запроса в сервисный слой.
     */
    @Test
    void checkoutDelegatesToService() {
        OrderGateway orderGateway = request -> new OrderProcessResponse("ORD-1", "COMPLETED", "RSV-1", "PAY-1", new BigDecimal("10.00"), "USD");
        CheckoutGatewayService checkoutGatewayService = new CheckoutGatewayService(orderGateway);
        CheckoutController checkoutController = new CheckoutController(checkoutGatewayService);

        CheckoutResponse response = checkoutController.checkout(new CheckoutRequest("customer-1", "SKU-1", 1, "SUCCESS"));

        assertThat(response.orderId()).isEqualTo("ORD-1");
        assertThat(response.status()).isEqualTo("COMPLETED");
    }
}
