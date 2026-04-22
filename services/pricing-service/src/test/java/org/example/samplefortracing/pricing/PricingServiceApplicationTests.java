package org.example.samplefortracing.pricing;

import org.junit.jupiter.api.Test;

/**
 * Проверяет базовые методы класса запуска сервиса расчёта цены.
 */
class PricingServiceApplicationTests {

    /**
     * Проверяет, что обработчик готовности приложения выполняется без ошибок.
     */
    @Test
    void logApplicationReadyDoesNotThrow() {
        new PricingServiceApplication().logApplicationReady();
    }
}
