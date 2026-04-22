package org.example.samplefortracing.gateway.api;

import org.example.samplefortracing.gateway.api.dto.CheckoutRequest;
import org.example.samplefortracing.gateway.api.dto.CheckoutResponse;
import org.example.samplefortracing.gateway.service.CheckoutGatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет внешний HTTP API для запуска checkout-потока.
 */
@RestController
@RequestMapping("/api/v1")
public class CheckoutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);

    private final CheckoutGatewayService checkoutGatewayService;

    /**
     * Создаёт контроллер checkout API.
     *
     * @param checkoutGatewayService сервис вызова процесса оформления заказа
     */
    public CheckoutController(CheckoutGatewayService checkoutGatewayService) {
        this.checkoutGatewayService = checkoutGatewayService;
    }

    /**
     * Запускает полный checkout-сценарий через сервис заказов.
     *
     * @param request входной запрос
     * @return итоговый результат оформления
     */
    @PostMapping("/checkout")
    public CheckoutResponse checkout(@RequestBody CheckoutRequest request) {
        LOGGER.info("Checkout requested for customer {} and item {}", request.customerId(), request.itemId());
        return checkoutGatewayService.checkout(request);
    }
}
