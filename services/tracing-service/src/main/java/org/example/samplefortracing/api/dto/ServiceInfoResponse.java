package org.example.samplefortracing.api.dto;

/**
 * Описывает базовую служебную информацию о микросервисе.
 *
 * @param serviceName имя сервиса
 * @param status текущее состояние сервиса
 */
public record ServiceInfoResponse(String serviceName, String status) {
}
