package org.example.samplefortracing.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Запускает Spring Boot Admin сервер для мониторинга локальных сервисов.
 */
@SpringBootApplication
@EnableAdminServer
public class AdminServerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationReady() {
        LOGGER.info("Spring Boot Admin is ready on http://localhost:19090");
    }
}
