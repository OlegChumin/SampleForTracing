package org.example.samplefortracing.api;

import org.example.samplefortracing.api.dto.ServiceInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет служебные HTTP endpoints для базовой проверки сервиса.
 */
@RestController
@RequestMapping("/api/v1/service")
public class ServiceInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInfoController.class);

    /**
     * Возвращает краткую информацию о текущем сервисе.
     *
     * @return ответ с именем сервиса и его состоянием
     */
    @GetMapping("/info")
    public ServiceInfoResponse getServiceInfo() {
        LOGGER.info("Service info endpoint was called");
        return new ServiceInfoResponse("tracing-service", "UP");
    }
}
