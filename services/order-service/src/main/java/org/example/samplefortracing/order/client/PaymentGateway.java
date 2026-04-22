package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.PaymentRequest;
import org.example.samplefortracing.order.client.dto.PaymentResponse;

/**
 * Определяет контракт вызова платёжного сервиса.
 */
public interface PaymentGateway {

    /**
     * Выполняет платёж через downstream сервис.
     *
     * @param request параметры платежа
     * @return результат оплаты
     */
    PaymentResponse charge(PaymentRequest request);
}
