package org.example.samplefortracing.pricing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Запускает микросервис расчёта цены заказа.
 */
@SpringBootApplication
public class PricingServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceApplication.class);

    /**
     * Запускает Spring Boot приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(PricingServiceApplication.class, args);
    }

    /**
     * Пишет сообщение о готовности сервиса.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationReady() {
        LOGGER.info("Pricing service is ready to accept requests");
    }
}
