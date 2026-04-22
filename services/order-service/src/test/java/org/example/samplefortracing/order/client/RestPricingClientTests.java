package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.PricingRequest;
import org.example.samplefortracing.order.client.dto.PricingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Проверяет REST-клиент сервиса расчёта цены.
 */
class RestPricingClientTests {

    /**
     * Проверяет корректный вызов endpoint расчёта цены.
     */
    @Test
    void calculateCallsPricingEndpoint() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8083");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestPricingClient restPricingClient = new RestPricingClient(builder.build());

        server.expect(requestTo("http://localhost:8083/api/v1/pricing/calculate"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(
                "{\"subtotal\":240.00,\"discountAmount\":0.00,\"taxAmount\":28.80,\"totalAmount\":268.80,\"currency\":\"USD\"}",
                MediaType.APPLICATION_JSON
            ));

        PricingResponse response = restPricingClient.calculate(new PricingRequest("customer-1", "SKU-1", 2));

        assertThat(response.currency()).isEqualTo("USD");
        server.verify();
    }
}
