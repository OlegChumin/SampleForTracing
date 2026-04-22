package org.example.samplefortracing.gateway;

import org.junit.jupiter.api.Test;

/**
 * Проверяет базовые методы класса запуска API gateway.
 */
class ApiGatewayApplicationTests {

    /**
     * Проверяет, что обработчик готовности приложения выполняется без ошибок.
     */
    @Test
    void logApplicationReadyDoesNotThrow() {
        new ApiGatewayApplication().logApplicationReady();
    }
}
