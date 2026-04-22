package org.example.samplefortracing.gateway.client;

import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.example.samplefortracing.gateway.client.dto.OrderSummaryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Проверяет REST-клиент сервиса заказов.
 */
class RestOrderClientTests {

    /**
     * Проверяет корректный вызов downstream endpoint сервиса заказов.
     */
    @Test
    void processCallsOrderServiceEndpoint() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8081");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestOrderClient restOrderClient = new RestOrderClient(builder.build());

        server.expect(requestTo("http://localhost:8081/api/v1/orders/process"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(
                "{\"orderId\":\"ORD-1\",\"orderStatus\":\"COMPLETED\",\"reservationId\":\"RSV-1\",\"paymentId\":\"PAY-1\",\"totalAmount\":268.80,\"currency\":\"USD\"}",
                MediaType.APPLICATION_JSON
            ));

        OrderProcessResponse response = restOrderClient.process(new OrderProcessRequest("customer-1", "SKU-1", 2, "SUCCESS"));

        assertThat(response.orderId()).isEqualTo("ORD-1");
        assertThat(response.totalAmount()).isEqualByComparingTo(BigDecimal.valueOf(268.8));
        server.verify();
    }

    /**
     * Проверяет корректный вызов endpoint чтения заказа.
     */
    @Test
    void getOrderCallsOrderServiceEndpoint() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8081");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestOrderClient restOrderClient = new RestOrderClient(builder.build());

        server.expect(requestTo("http://localhost:8081/api/v1/orders/ORD-1"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(
                "{\"orderId\":\"ORD-1\",\"customerId\":\"customer-1\",\"itemId\":\"SKU-1\",\"quantity\":2,\"status\":\"COMPLETED\",\"totalAmount\":268.80,\"currency\":\"USD\"}",
                MediaType.APPLICATION_JSON
            ));

        OrderSummaryResponse response = restOrderClient.getOrder("ORD-1");

        assertThat(response.orderId()).isEqualTo("ORD-1");
        assertThat(response.status()).isEqualTo("COMPLETED");
        server.verify();
    }
}
