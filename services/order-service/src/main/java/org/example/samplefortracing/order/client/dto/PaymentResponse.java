package org.example.samplefortracing.order.client.dto;

/**
 * Описывает ответ платёжного сервиса.
 *
 * @param paymentId идентификатор платежа
 * @param status состояние платежа
 * @param message описание результата
 */
public record PaymentResponse(String paymentId, String status, String message) {
}
