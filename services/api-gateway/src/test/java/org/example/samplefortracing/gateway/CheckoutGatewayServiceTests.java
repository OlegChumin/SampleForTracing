package org.example.samplefortracing.gateway;

import org.example.samplefortracing.gateway.api.dto.CheckoutRequest;
import org.example.samplefortracing.gateway.api.dto.CheckoutResponse;
import org.example.samplefortracing.gateway.client.OrderGateway;
import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.example.samplefortracing.gateway.client.dto.OrderSummaryResponse;
import org.example.samplefortracing.gateway.service.CheckoutGatewayService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет внешний gateway сценария оформления заказа.
 */
class CheckoutGatewayServiceTests {

    /**
     * Проверяет успешное делегирование checkout-процесса в сервис заказов.
     */
    @Test
    void checkoutReturnsAdaptedOrderResponse() {
        OrderGateway orderGateway = new StubOrderGateway();
        CheckoutGatewayService checkoutGatewayService = new CheckoutGatewayService(orderGateway);

        CheckoutResponse response = checkoutGatewayService.checkout(
            new CheckoutRequest("customer-1", "SKU-CHAIR-01", 2, "SUCCESS")
        );

        assertThat(response.orderId()).isEqualTo("ORD-test");
        assertThat(response.status()).isEqualTo("COMPLETED");
        assertThat(response.totalAmount()).isEqualByComparingTo("268.80");
    }

    /**
     * Проверяет получение сохранённого состояния заказа через gateway service.
     */
    @Test
    void getOrderReturnsOrderSummary() {
        OrderGateway orderGateway = new StubOrderGateway();
        CheckoutGatewayService checkoutGatewayService = new CheckoutGatewayService(orderGateway);

        OrderSummaryResponse response = checkoutGatewayService.getOrder("ORD-test");

        assertThat(response.orderId()).isEqualTo("ORD-test");
        assertThat(response.status()).isEqualTo("COMPLETED");
    }

    /**
     * Возвращает тестовый ответ сервиса заказов.
     */
    private static class StubOrderGateway implements OrderGateway {

        @Override
        public OrderProcessResponse process(OrderProcessRequest request) {
            return new OrderProcessResponse(
                "ORD-test",
                "COMPLETED",
                "RSV-test",
                "PAY-test",
                new BigDecimal("268.80"),
                "USD"
            );
        }

        @Override
        public OrderSummaryResponse getOrder(String orderId) {
            return new OrderSummaryResponse(orderId, "customer-1", "SKU-CHAIR-01", 2, "COMPLETED", new BigDecimal("268.80"), "USD");
        }
    }
}
