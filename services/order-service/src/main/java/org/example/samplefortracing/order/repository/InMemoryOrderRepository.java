package org.example.samplefortracing.order.repository;

import org.example.samplefortracing.order.service.model.OrderSnapshot;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Хранит заказы в памяти для демонстрационного сценария.
 */
@Repository
public class InMemoryOrderRepository {

    private final Map<String, OrderSnapshot> storage = new ConcurrentHashMap<>();

    /**
     * Сохраняет состояние заказа.
     *
     * @param orderSnapshot состояние заказа
     * @return сохранённое состояние
     */
    public OrderSnapshot save(OrderSnapshot orderSnapshot) {
        storage.put(orderSnapshot.orderId(), orderSnapshot);
        return orderSnapshot;
    }

    /**
     * Ищет заказ по идентификатору.
     *
     * @param orderId идентификатор заказа
     * @return найденный заказ или пустой результат
     */
    public Optional<OrderSnapshot> findById(String orderId) {
        return Optional.ofNullable(storage.get(orderId));
    }
}
