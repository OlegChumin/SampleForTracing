package org.example.samplefortracing.order.event;

/**
 * Публикует checkout-события в транспорт событий.
 */
public interface CheckoutEventPublisher {

    /**
     * Публикует событие создания заказа.
     *
     * @param event событие создания заказа
     */
    void publishOrderCreated(OrderCreatedEvent event);

    /**
     * Публикует событие завершения заказа.
     *
     * @param event событие завершения заказа
     */
    void publishOrderCompleted(OrderCompletedEvent event);
}
