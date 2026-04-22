package org.example.samplefortracing.pricing.api;

import org.example.samplefortracing.pricing.api.dto.PricingRequest;
import org.example.samplefortracing.pricing.api.dto.PricingResponse;
import org.example.samplefortracing.pricing.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет HTTP-контроллер сервиса расчёта цены.
 */
class PricingControllerTests {

    /**
     * Проверяет делегирование расчёта в сервисный слой.
     */
    @Test
    void calculateDelegatesToService() {
        PricingController pricingController = new PricingController(new PricingService());

        PricingResponse response = pricingController.calculate(new PricingRequest("customer-1", "SKU-CHAIR-01", 2));

        assertThat(response.totalAmount()).isEqualByComparingTo("268.80");
    }
}
