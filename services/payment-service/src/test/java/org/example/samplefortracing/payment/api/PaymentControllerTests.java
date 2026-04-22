package org.example.samplefortracing.payment.api;

import org.example.samplefortracing.payment.api.dto.PaymentRequest;
import org.example.samplefortracing.payment.api.dto.PaymentResponse;
import org.example.samplefortracing.payment.service.PaymentSimulationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет HTTP-контроллер платёжного сервиса.
 */
class PaymentControllerTests {

    /**
     * Проверяет делегирование запроса оплаты в сервисный слой.
     */
    @Test
    void chargeDelegatesToService() {
        PaymentController paymentController = new PaymentController(new PaymentSimulationService());

        PaymentResponse response = paymentController.charge(
            new PaymentRequest("ORD-1", "customer-1", new BigDecimal("25.00"), "USD", "SUCCESS")
        );

        assertThat(response.status()).isEqualTo("COMPLETED");
    }
}
