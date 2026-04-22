package org.example.samplefortracing.gateway.client;

import org.example.samplefortracing.gateway.client.dto.OrderProcessRequest;
import org.example.samplefortracing.gateway.client.dto.OrderProcessResponse;
import org.example.samplefortracing.gateway.client.dto.OrderSummaryResponse;

/**
 * Определяет контракт вызова сервиса заказов.
 */
public interface OrderGateway {

    /**
     * Запускает checkout-процесс в сервисе заказов.
     *
     * @param request параметры заказа
     * @return результат оформления
     */
    OrderProcessResponse process(OrderProcessRequest request);

    /**
     * Возвращает краткую информацию по уже созданному заказу.
     *
     * @param orderId идентификатор заказа
     * @return сохранённое состояние заказа
     */
    OrderSummaryResponse getOrder(String orderId);
}
