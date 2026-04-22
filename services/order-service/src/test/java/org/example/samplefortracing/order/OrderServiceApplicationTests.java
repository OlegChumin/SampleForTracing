package org.example.samplefortracing.order;

import org.junit.jupiter.api.Test;

/**
 * Проверяет базовые методы класса запуска сервиса заказов.
 */
class OrderServiceApplicationTests {

    /**
     * Проверяет, что обработчик готовности приложения выполняется без ошибок.
     */
    @Test
    void logApplicationReadyDoesNotThrow() {
        new OrderServiceApplication().logApplicationReady();
    }
}
