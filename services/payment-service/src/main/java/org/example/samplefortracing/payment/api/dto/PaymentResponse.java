package org.example.samplefortracing.payment.api.dto;

/**
 * Описывает результат обработки оплаты.
 *
 * @param paymentId идентификатор платежа
 * @param status состояние платежа
 * @param message текстовое описание результата
 */
public record PaymentResponse(String paymentId, String status, String message) {
}
