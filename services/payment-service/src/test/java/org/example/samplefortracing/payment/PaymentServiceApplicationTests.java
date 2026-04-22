package org.example.samplefortracing.payment;

import org.junit.jupiter.api.Test;

/**
 * Проверяет базовые методы класса запуска платёжного сервиса.
 */
class PaymentServiceApplicationTests {

    /**
     * Проверяет, что обработчик готовности приложения выполняется без ошибок.
     */
    @Test
    void logApplicationReadyDoesNotThrow() {
        new PaymentServiceApplication().logApplicationReady();
    }
}
