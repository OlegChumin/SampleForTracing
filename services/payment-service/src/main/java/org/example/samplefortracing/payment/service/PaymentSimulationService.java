package org.example.samplefortracing.payment.service;

import org.example.samplefortracing.payment.api.dto.PaymentRequest;
import org.example.samplefortracing.payment.api.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.UUID;

/**
 * Выполняет имитацию различных сценариев работы платёжного шлюза.
 */
@Service
public class PaymentSimulationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSimulationService.class);

    /**
     * Обрабатывает платёж в соответствии с указанным сценарием.
     *
     * @param request параметры платежа
     * @return результат обработки
     */
    public PaymentResponse charge(PaymentRequest request) {
        String scenario = normalizeScenario(request.paymentScenario());
        LOGGER.info("Payment scenario {} started for order {}", scenario, request.orderId());

        return switch (scenario) {
            case "DECLINED" -> throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Payment was declined");
            case "CONFLICT" -> throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment requires retry");
            case "ERROR" -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment provider failed");
            case "DELAYED" -> delayedSuccess(request.orderId());
            default -> success("Payment completed");
        };
    }

    /**
     * Нормализует входной сценарий оплаты.
     *
     * @param scenario входной сценарий
     * @return нормализованное значение
     */
    String normalizeScenario(String scenario) {
        if (scenario == null || scenario.isBlank()) {
            return "SUCCESS";
        }
        return scenario.toUpperCase(Locale.ROOT);
    }

    /**
     * Выполняет успешный платёж с искусственной задержкой.
     *
     * @param orderId идентификатор заказа
     * @return результат успешного платежа
     */
    PaymentResponse delayedSuccess(String orderId) {
        LOGGER.info("Payment scenario DELAYED is sleeping before completion for order {}", orderId);
        try {
            Thread.sleep(1200L);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Payment delay was interrupted");
        }
        return success("Payment completed after delay");
    }

    /**
     * Формирует успешный ответ платёжного сервиса.
     *
     * @param message текстовое описание результата
     * @return результат успешного платежа
     */
    PaymentResponse success(String message) {
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        LOGGER.info("Payment {} completed successfully", paymentId);
        return new PaymentResponse(paymentId, "COMPLETED", message);
    }
}
