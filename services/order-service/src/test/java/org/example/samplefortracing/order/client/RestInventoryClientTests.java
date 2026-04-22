package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.InventoryReservationRequest;
import org.example.samplefortracing.order.client.dto.InventoryReservationResponse;
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
 * Проверяет REST-клиент сервиса остатков.
 */
class RestInventoryClientTests {

    /**
     * Проверяет корректный вызов сервиса резервирования остатков.
     */
    @Test
    void reserveCallsInventoryEndpoint() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://localhost:8082");
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        RestInventoryClient restInventoryClient = new RestInventoryClient(builder.build());

        server.expect(requestTo("http://localhost:8082/api/v1/inventory/reservations"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess(
                "{\"reservationId\":\"RSV-1\",\"itemId\":\"SKU-1\",\"reservedQuantity\":2,\"status\":\"RESERVED\"}",
                MediaType.APPLICATION_JSON
            ));

        InventoryReservationResponse response = restInventoryClient.reserve(new InventoryReservationRequest("SKU-1", 2));

        assertThat(response.reservationId()).isEqualTo("RSV-1");
        server.verify();
    }
}
