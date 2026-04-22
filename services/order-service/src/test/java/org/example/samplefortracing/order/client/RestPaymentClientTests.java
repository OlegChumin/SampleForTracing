package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.PaymentRequest;
import org.example.samplefortracing.order.client.dto.PaymentResponse;
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
 * Проверяет REST-клиент платёжного сервиса.
 */
class RestPaymentClientTests {

    /**
     * Проверяет корректный вызов endpoint оплаты.
     */
    @Test
    void chargeCallsPaymentEndpoint() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8084");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestPaymentClient restPaymentClient = new RestPaymentClient(builder.build());

        server.expect(requestTo("http://localhost:8084/api/v1/payments/charge"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(
                "{\"paymentId\":\"PAY-1\",\"status\":\"COMPLETED\",\"message\":\"Payment completed\"}",
                MediaType.APPLICATION_JSON
            ));

        PaymentResponse response = restPaymentClient.charge(
            new PaymentRequest("ORD-1", "customer-1", new BigDecimal("50.00"), "USD", "SUCCESS")
        );

        assertThat(response.paymentId()).isEqualTo("PAY-1");
        server.verify();
    }
}
