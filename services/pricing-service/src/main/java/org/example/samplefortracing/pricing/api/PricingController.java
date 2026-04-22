package org.example.samplefortracing.pricing.api;

import org.example.samplefortracing.pricing.api.dto.PricingRequest;
import org.example.samplefortracing.pricing.api.dto.PricingResponse;
import org.example.samplefortracing.pricing.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Предоставляет HTTP API для расчёта стоимости заказа.
 */
@RestController
@RequestMapping("/api/v1/pricing")
public class PricingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingController.class);

    private final PricingService pricingService;

    /**
     * Создаёт контроллер расчёта цены.
     *
     * @param pricingService сервис бизнес-логики ценообразования
     */
    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    /**
     * Рассчитывает итоговую стоимость заказа.
     *
     * @param request входной запрос на расчёт
     * @return рассчитанная цена
     */
    @PostMapping("/calculate")
    public PricingResponse calculate(@RequestBody PricingRequest request) {
        LOGGER.info("Pricing calculation requested for customer {} and item {}", request.customerId(), request.itemId());
        return pricingService.calculate(request);
    }
}
