package org.example.samplefortracing;

import org.example.samplefortracing.api.ServiceInfoController;
import org.example.samplefortracing.api.dto.ServiceInfoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет, что стартовый микросервис поднимает контекст и отвечает по базовому endpoint.
 */
@SpringBootTest
class SampleForTracingApplicationTests {

    @Autowired
    private ServiceInfoController serviceInfoController;

    /**
     * Проверяет успешный запуск Spring контекста.
     */
    @Test
    void contextLoads() {
        assertThat(serviceInfoController).isNotNull();
    }

    /**
     * Проверяет доступность служебной информации о сервисе.
     */
    @Test
    void serviceInfoEndpointReturnsExpectedPayload() {
        ServiceInfoResponse response = serviceInfoController.getServiceInfo();

        assertThat(response.serviceName()).isEqualTo("tracing-service");
        assertThat(response.status()).isEqualTo("UP");
    }
}
