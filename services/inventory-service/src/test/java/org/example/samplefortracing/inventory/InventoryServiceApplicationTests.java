package org.example.samplefortracing.inventory;

import org.junit.jupiter.api.Test;

/**
 * Проверяет базовые методы класса запуска сервиса остатков.
 */
class InventoryServiceApplicationTests {

    /**
     * Проверяет, что обработчик готовности приложения выполняется без ошибок.
     */
    @Test
    void logApplicationReadyDoesNotThrow() {
        new InventoryServiceApplication().logApplicationReady();
    }
}
