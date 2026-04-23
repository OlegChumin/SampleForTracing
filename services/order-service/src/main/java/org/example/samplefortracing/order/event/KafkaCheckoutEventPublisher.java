package org.example.samplefortracing.order.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Публикует checkout-события в Kafka.
 */
@Component
public class KafkaCheckoutEventPublisher implements CheckoutEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCheckoutEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderCreatedTopic;
    private final String orderCompletedTopic;

    /**
     * Создаёт Kafka publisher для checkout-событий.
     *
     * @param kafkaTemplate шаблон отправки Kafka-сообщений
     * @param orderCreatedTopic топик событий создания заказа
     * @param orderCompletedTopic топик событий завершения заказа
     */
    public KafkaCheckoutEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                       @Value("${checkout.kafka.topics.order-created}") String orderCreatedTopic,
                                       @Value("${checkout.kafka.topics.order-completed}") String orderCompletedTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderCreatedTopic = orderCreatedTopic;
        this.orderCompletedTopic = orderCompletedTopic;
    }

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        LOGGER.info("Publishing order-created event {} for order {}", event.eventId(), event.orderId());
        kafkaTemplate.send(orderCreatedTopic, event.orderId(), event)
            .whenComplete((result, throwable) -> logResult("order-created", event.eventId(), event.orderId(), throwable));
    }

    @Override
    public void publishOrderCompleted(OrderCompletedEvent event) {
        LOGGER.info("Publishing order-completed event {} for order {}", event.eventId(), event.orderId());
        kafkaTemplate.send(orderCompletedTopic, event.orderId(), event)
            .whenComplete((result, throwable) -> logResult("order-completed", event.eventId(), event.orderId(), throwable));
    }

    private void logResult(String eventType, String eventId, String orderId, Throwable throwable) {
        if (throwable == null) {
            LOGGER.info("Published {} event {} for order {}", eventType, eventId, orderId);
            return;
        }
        LOGGER.error("Failed to publish {} event {} for order {}", eventType, eventId, orderId, throwable);
    }
}
