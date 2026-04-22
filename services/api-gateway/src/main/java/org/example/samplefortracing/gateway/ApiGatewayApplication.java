package org.example.samplefortracing.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Запускает внешний API gateway для checkout-сценария.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGatewayApplication.class);

    /**
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    /**
     * Пишет сообщение о готовности сервиса.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationReady() {
        LOGGER.info("API gateway is ready to accept requests");
    }
}
