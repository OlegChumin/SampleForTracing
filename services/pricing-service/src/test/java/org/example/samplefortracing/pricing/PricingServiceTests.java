package org.example.samplefortracing.pricing;

import org.example.samplefortracing.pricing.api.dto.PricingRequest;
import org.example.samplefortracing.pricing.api.dto.PricingResponse;
import org.example.samplefortracing.pricing.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет базовые сценарии расчёта стоимости заказа.
 */
class PricingServiceTests {

    private final PricingService pricingService = new PricingService();

    /**
     * Проверяет расчёт цены без дополнительных скидок.
     */
    @Test
    void calculateReturnsExpectedTotalForRegularCustomer() {
        PricingResponse response = pricingService.calculate(new PricingRequest("customer-1", "SKU-CHAIR-01", 2));

        assertThat(response.subtotal()).isEqualByComparingTo("240.00");
        assertThat(response.discountAmount()).isEqualByComparingTo("0.00");
        assertThat(response.taxAmount()).isEqualByComparingTo("28.80");
        assertThat(response.totalAmount()).isEqualByComparingTo("268.80");
    }

    /**
     * Проверяет применение скидки для VIP клиента.
     */
    @Test
    void calculateAppliesVipDiscount() {
        PricingResponse response = pricingService.calculate(new PricingRequest("vip-customer-1", "SKU-LAMP-03", 4));

        assertThat(response.discountAmount()).isEqualByComparingTo("18.00");
        assertThat(response.totalAmount()).isEqualByComparingTo("181.44");
    }

    /**
     * Проверяет скидку по количеству для обычного клиента.
     */
    @Test
    void calculateAppliesQuantityDiscount() {
        PricingResponse response = pricingService.calculate(new PricingRequest("customer-1", "SKU-LAMP-03", 5));

        assertThat(response.discountAmount()).isEqualByComparingTo("11.25");
        assertThat(response.totalAmount()).isEqualByComparingTo("239.40");
    }

    /**
     * Проверяет использование цены по умолчанию для неизвестного товара.
     */
    @Test
    void calculateUsesFallbackPriceForUnknownItem() {
        PricingResponse response = pricingService.calculate(new PricingRequest("customer-1", "UNKNOWN", 1));

        assertThat(response.subtotal()).isEqualByComparingTo("99.00");
        assertThat(response.totalAmount()).isEqualByComparingTo("110.88");
    }
}
