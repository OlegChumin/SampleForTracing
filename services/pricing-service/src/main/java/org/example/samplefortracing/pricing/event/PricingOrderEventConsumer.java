package org.example.samplefortracing.pricing.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Обрабатывает Kafka-события заказа в сервисе расчёта цены.
 */
@Component
public class PricingOrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingOrderEventConsumer.class);

    private final ObjectMapper objectMapper;

    /**
     * Создаёт consumer событий заказа.
     *
     * @param objectMapper mapper JSON-сообщений
     */
    public PricingOrderEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Обрабатывает событие создания заказа.
     *
     * @param payload JSON-сообщение из Kafka
     */
    @KafkaListener(topics = "${checkout.kafka.topics.order-created}", groupId = "${spring.application.name}")
    public void handleOrderCreated(String payload) {
        OrderCreatedEvent event = readEvent(payload);
        LOGGER.info(
            "Pricing audit received order-created event {} for order {} and customer {}",
            event.eventId(),
            event.orderId(),
            event.customerId()
        );
    }

    private OrderCreatedEvent readEvent(String payload) {
        try {
            return objectMapper.readValue(payload, OrderCreatedEvent.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid order-created event payload", exception);
        }
    }
}
