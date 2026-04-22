package org.example.samplefortracing.gateway.api.dto;

/**
 * Описывает текущее состояние внешнего сервиса для панели управления.
 *
 * @param serviceName имя сервиса
 * @param baseUrl базовый URL сервиса
 * @param status состояние доступности
 */
public record ServiceStatusResponse(String serviceName, String baseUrl, String status) {
}
