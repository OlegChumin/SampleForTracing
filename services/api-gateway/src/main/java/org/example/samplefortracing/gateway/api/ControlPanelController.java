package org.example.samplefortracing.gateway.api;

import org.example.samplefortracing.gateway.api.dto.ServiceStatusResponse;
import org.example.samplefortracing.gateway.client.dto.OrderSummaryResponse;
import org.example.samplefortracing.gateway.service.CheckoutGatewayService;
import org.example.samplefortracing.gateway.service.ServiceMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Предоставляет API панели управления локальным демо-стендом.
 */
@RestController
@RequestMapping("/api/v1/control-panel")
public class ControlPanelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlPanelController.class);

    private final CheckoutGatewayService checkoutGatewayService;
    private final ServiceMonitorService serviceMonitorService;

    /**
     * Создаёт контроллер панели управления.
     *
     * @param checkoutGatewayService сервис доступа к checkout-операциям
     * @param serviceMonitorService сервис проверки доступности сервисов
     */
    public ControlPanelController(CheckoutGatewayService checkoutGatewayService,
                                  ServiceMonitorService serviceMonitorService) {
        this.checkoutGatewayService = checkoutGatewayService;
        this.serviceMonitorService = serviceMonitorService;
    }

    /**
     * Возвращает краткое состояние всех сервисов демо-стенда.
     *
     * @return список статусов сервисов
     */
    @GetMapping("/services")
    public List<ServiceStatusResponse> getServices() {
        LOGGER.info("Control panel requested service status snapshot");
        return serviceMonitorService.getServiceStatuses();
    }

    /**
     * Возвращает сохранённое состояние заказа.
     *
     * @param orderId идентификатор заказа
     * @return информация о заказе
     */
    @GetMapping("/orders/{orderId}")
    public OrderSummaryResponse getOrder(@PathVariable String orderId) {
        LOGGER.info("Control panel requested order {}", orderId);
        return checkoutGatewayService.getOrder(orderId);
    }
}
