package org.example.samplefortracing.payment;

import org.example.samplefortracing.payment.api.dto.PaymentRequest;
import org.example.samplefortracing.payment.api.dto.PaymentResponse;
import org.example.samplefortracing.payment.service.PaymentSimulationService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Проверяет основные сценарии симуляции оплаты.
 */
class PaymentSimulationServiceTests {

    private final PaymentSimulationService paymentSimulationService = new PaymentSimulationService();

    /**
     * Проверяет успешный сценарий оплаты.
     */
    @Test
    void chargeReturnsCompletedPaymentForSuccessScenario() {
        PaymentResponse response = paymentSimulationService.charge(
            new PaymentRequest("ORD-1", "customer-1", new BigDecimal("50.00"), "USD", "SUCCESS")
        );

        assertThat(response.status()).isEqualTo("COMPLETED");
    }

    /**
     * Проверяет ошибку при отклонённом платеже.
     */
    @Test
    void chargeFailsForDeclinedScenario() {
        assertThatThrownBy(() -> paymentSimulationService.charge(
            new PaymentRequest("ORD-2", "customer-1", new BigDecimal("80.00"), "USD", "DECLINED")
        )).isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("422 UNPROCESSABLE_ENTITY");
    }

    /**
     * Проверяет конфликтный сценарий оплаты.
     */
    @Test
    void chargeFailsForConflictScenario() {
        assertThatThrownBy(() -> paymentSimulationService.charge(
            new PaymentRequest("ORD-3", "customer-1", new BigDecimal("80.00"), "USD", "CONFLICT")
        )).isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("409 CONFLICT");
    }

    /**
     * Проверяет сценарий внутренней ошибки платёжного шлюза.
     */
    @Test
    void chargeFailsForErrorScenario() {
        assertThatThrownBy(() -> paymentSimulationService.charge(
            new PaymentRequest("ORD-4", "customer-1", new BigDecimal("80.00"), "USD", "ERROR")
        )).isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("500 INTERNAL_SERVER_ERROR");
    }

    /**
     * Проверяет нормализацию пустого сценария к успешной оплате.
     */
    @Test
    void chargeUsesSuccessScenarioForBlankValue() {
        PaymentResponse response = paymentSimulationService.charge(
            new PaymentRequest("ORD-5", "customer-1", new BigDecimal("80.00"), "USD", " ")
        );

        assertThat(response.status()).isEqualTo("COMPLETED");
    }

    /**
     * Проверяет успешный сценарий оплаты с задержкой.
     */
    @Test
    void chargeCompletesForDelayedScenario() {
        PaymentResponse response = paymentSimulationService.charge(
            new PaymentRequest("ORD-6", "customer-1", new BigDecimal("80.00"), "USD", "DELAYED")
        );

        assertThat(response.status()).isEqualTo("COMPLETED");
        assertThat(response.message()).contains("delay");
    }
}
