package org.example.samplefortracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Запускает стартовый микросервис для экспериментов с трассировкой.
 */
@SpringBootApplication
public class SampleForTracingApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleForTracingApplication.class);

    /**
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(SampleForTracingApplication.class, args);
    }

    /**
     * Пишет стартовое сообщение после полной инициализации контекста.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationReady() {
        LOGGER.info("Tracing service is ready to accept requests");
    }
}
