package org.example.samplefortracing.gateway.service;

import org.example.samplefortracing.gateway.api.dto.ServiceStatusResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Проверяет мониторинг статуса сервисов в API gateway.
 */
class ServiceMonitorServiceTests {

    /**
     * Проверяет сбор статусов сервисов по actuator health endpoint.
     */
    @Test
    void getServiceStatusesCollectsStatuses() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        ServiceMonitorService serviceMonitorService = new ServiceMonitorService(
            builder,
            "http://localhost:8081",
            "http://localhost:8082",
            "http://localhost:8083",
            "http://localhost:8084"
        );

        server.expect(requestTo("http://localhost:8081/actuator/health"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"status\":\"UP\"}", MediaType.APPLICATION_JSON));
        server.expect(requestTo("http://localhost:8082/actuator/health"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"status\":\"UP\"}", MediaType.APPLICATION_JSON));
        server.expect(requestTo("http://localhost:8083/actuator/health"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"status\":\"UP\"}", MediaType.APPLICATION_JSON));
        server.expect(requestTo("http://localhost:8084/actuator/health"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("{\"status\":\"UP\"}", MediaType.APPLICATION_JSON));

        List<ServiceStatusResponse> responses = serviceMonitorService.getServiceStatuses();

        assertThat(responses).hasSize(5);
        assertThat(responses.get(1).status()).isEqualTo("UP");
        server.verify();
    }
}
