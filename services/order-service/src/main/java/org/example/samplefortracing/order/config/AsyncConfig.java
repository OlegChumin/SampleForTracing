package org.example.samplefortracing.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Создаёт инфраструктуру для параллельных REST-вызовов внутри сервиса заказов.
 */
@Configuration
public class AsyncConfig {

    /**
     * Создаёт пул потоков для fan-out вызовов к downstream сервисам.
     *
     * @return пул потоков фиксированного размера
     */
    @Bean(destroyMethod = "shutdown")
    public ExecutorService orderTaskExecutor() {
        return Executors.newFixedThreadPool(4);
    }
}
