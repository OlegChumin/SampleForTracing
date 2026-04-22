package org.example.samplefortracing.pricing.service;

import org.example.samplefortracing.pricing.api.dto.PricingRequest;
import org.example.samplefortracing.pricing.api.dto.PricingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Выполняет расчёт стоимости заказа, скидок и налога.
 */
@Service
public class PricingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingService.class);

    private static final Map<String, BigDecimal> PRICE_LIST = Map.of(
        "SKU-CHAIR-01", new BigDecimal("120.00"),
        "SKU-DESK-02", new BigDecimal("320.00"),
        "SKU-LAMP-03", new BigDecimal("45.00"),
        "SKU-MONITOR-04", new BigDecimal("410.00")
    );

    /**
     * Рассчитывает стоимость заказа.
     *
     * @param request входной запрос
     * @return результат расчёта
     */
    public PricingResponse calculate(PricingRequest request) {
        BigDecimal unitPrice = loadUnitPrice(request.itemId());
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(request.quantity()));
        BigDecimal discountRate = loadDiscountRate(request.customerId(), request.quantity());
        BigDecimal discountAmount = subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = calculateTax(taxableAmount);
        BigDecimal totalAmount = taxableAmount.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        LOGGER.info("Pricing calculated total {} for customer {}", totalAmount, request.customerId());
        return new PricingResponse(subtotal, discountAmount, taxAmount, totalAmount, "USD");
    }

    /**
     * Загружает базовую цену товара.
     *
     * @param itemId идентификатор товара
     * @return цена за единицу
     */
    BigDecimal loadUnitPrice(String itemId) {
        LOGGER.info("Loading price list entry for item {}", itemId);
        return PRICE_LIST.getOrDefault(itemId, new BigDecimal("99.00"));
    }

    /**
     * Загружает ставку скидки для клиента.
     *
     * @param customerId идентификатор клиента
     * @param quantity количество единиц товара
     * @return доля скидки
     */
    BigDecimal loadDiscountRate(String customerId, int quantity) {
        LOGGER.info("Loading discount rules for customer {}", customerId);
        if (customerId != null && customerId.startsWith("vip")) {
            return new BigDecimal("0.10");
        }
        if (quantity >= 5) {
            return new BigDecimal("0.05");
        }
        return BigDecimal.ZERO;
    }

    /**
     * Рассчитывает налог на сумму после скидки.
     *
     * @param taxableAmount сумма, облагаемая налогом
     * @return сумма налога
     */
    BigDecimal calculateTax(BigDecimal taxableAmount) {
        LOGGER.info("Calculating tax for amount {}", taxableAmount);
        return taxableAmount.multiply(new BigDecimal("0.12")).setScale(2, RoundingMode.HALF_UP);
    }
}
