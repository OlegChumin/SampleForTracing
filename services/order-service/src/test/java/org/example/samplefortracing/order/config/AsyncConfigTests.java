package org.example.samplefortracing.order.config;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет конфигурацию асинхронного выполнения сервиса заказов.
 */
class AsyncConfigTests {

    /**
     * Проверяет создание пула потоков для fan-out вызовов.
     */
    @Test
    void orderTaskExecutorCreatesExecutorService() {
        AsyncConfig asyncConfig = new AsyncConfig();

        ExecutorService executorService = asyncConfig.orderTaskExecutor();

        assertThat(executorService).isNotNull();
        executorService.shutdown();
    }
}
