package org.example.samplefortracing.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Запускает микросервис оркестрации оформления заказа.
 */
@SpringBootApplication
public class OrderServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceApplication.class);

    /**
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    /**
     * Пишет сообщение о готовности сервиса.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationReady() {
        LOGGER.info("Order service is ready to accept requests");
    }
}
