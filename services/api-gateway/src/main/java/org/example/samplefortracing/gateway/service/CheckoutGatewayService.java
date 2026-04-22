package org.example.samplefortracing.gateway.service;

import org.example.samplefortracing.gateway.api.dto.CheckoutRequest;
import org.example.samplefortracing.gateway.api.dto.CheckoutResponse;
import org.example.samplefortracing.gateway.client.OrderGateway;
import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Делегирует checkout-процесс в сервис заказов.
 */
@Service
public class CheckoutGatewayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutGatewayService.class);

    private final OrderGateway orderGateway;

    /**
     * Создаёт сервис gateway-оркестрации.
     *
     * @param orderGateway клиент сервиса заказов
     */
    public CheckoutGatewayService(OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    /**
     * Запускает checkout и адаптирует ответ сервиса заказов к внешнему API.
     *
     * @param request входной запрос
     * @return итоговый ответ gateway
     */
    public CheckoutResponse checkout(CheckoutRequest request) {
        OrderProcessResponse response = orderGateway.process(
            new OrderProcessRequest(request.customerId(), request.itemId(), request.quantity(), request.paymentScenario())
        );
        LOGGER.info("Checkout flow completed for order {}", response.orderId());
        return new CheckoutResponse(
            response.orderId(),
            response.orderStatus(),
            response.reservationId(),
            response.paymentId(),
            response.totalAmount(),
            response.currency()
        );
    }
}
