package org.example.samplefortracing.order.api;

import org.example.samplefortracing.order.api.dto.OrderProcessRequest;
import org.example.samplefortracing.order.api.dto.OrderProcessResponse;
import org.example.samplefortracing.order.api.dto.OrderSummaryResponse;
import org.example.samplefortracing.order.service.OrderProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет HTTP API для оформления и чтения заказов.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderProcessingService orderProcessingService;

    /**
     * Создаёт контроллер заказов.
     *
     * @param orderProcessingService сервис оркестрации заказа
     */
    public OrderController(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    /**
     * Полностью обрабатывает checkout-поток заказа.
     *
     * @param request параметры заказа
     * @return итоговый результат оформления
     */
    @PostMapping("/process")
    public OrderProcessResponse process(@RequestBody OrderProcessRequest request) {
        LOGGER.info("Order processing requested for customer {} and item {}", request.customerId(), request.itemId());
        return orderProcessingService.process(request);
    }

    /**
     * Возвращает сохранённое краткое состояние заказа.
     *
     * @param orderId идентификатор заказа
     * @return сохранённый заказ
     */
    @GetMapping("/{orderId}")
    public OrderSummaryResponse getOrder(@PathVariable String orderId) {
        LOGGER.info("Order summary requested for order {}", orderId);
        return orderProcessingService.getOrder(orderId);
    }
}
