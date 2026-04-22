package org.example.samplefortracing.payment.api;

import org.example.samplefortracing.payment.api.dto.PaymentRequest;
import org.example.samplefortracing.payment.api.dto.PaymentResponse;
import org.example.samplefortracing.payment.service.PaymentSimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет HTTP API для имитации платёжных операций.
 */
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentSimulationService paymentSimulationService;

    /**
     * Создаёт контроллер платёжных операций.
     *
     * @param paymentSimulationService сервис симуляции оплаты
     */
    public PaymentController(PaymentSimulationService paymentSimulationService) {
        this.paymentSimulationService = paymentSimulationService;
    }

    /**
     * Выполняет имитацию списания средств.
     *
     * @param request параметры платежа
     * @return результат платежа
     */
    @PostMapping("/charge")
    public PaymentResponse charge(@RequestBody PaymentRequest request) {
        LOGGER.info("Payment requested for order {} with scenario {}", request.orderId(), request.paymentScenario());
        return paymentSimulationService.charge(request);
    }
}
