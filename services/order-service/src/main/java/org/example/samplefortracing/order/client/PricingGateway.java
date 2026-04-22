package org.example.samplefortracing.order.client;

import org.example.samplefortracing.order.client.dto.PricingRequest;
import org.example.samplefortracing.order.client.dto.PricingResponse;

/**
 * Определяет контракт вызова сервиса расчёта цены.
 */
public interface PricingGateway {

    /**
     * Запрашивает расчёт стоимости в downstream сервисе.
     *
     * @param request параметры расчёта
     * @return рассчитанная цена
     */
    PricingResponse calculate(PricingRequest request);
}
