package org.example.samplefortracing.order.repository;

import org.example.samplefortracing.order.service.model.OrderSnapshot;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверяет репозиторий заказов в памяти.
 */
class InMemoryOrderRepositoryTests {

    /**
     * Проверяет сохранение и чтение заказа.
     */
    @Test
    void saveStoresOrderSnapshot() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        OrderSnapshot orderSnapshot = new OrderSnapshot("ORD-1", "customer-1", "SKU-1", 1, "COMPLETED", new BigDecimal("10.00"), "USD");

        repository.save(orderSnapshot);

        assertThat(repository.findById("ORD-1")).contains(orderSnapshot);
    }
}
