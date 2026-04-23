package org.example.samplefortracing.payment.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Обрабатывает Kafka-события заказа в платёжном сервисе.
 */
@Component
public class PaymentOrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentOrderEventConsumer.class);

    private final ObjectMapper objectMapper;

    /**
     * Создаёт consumer событий заказа.
     *
     * @param objectMapper mapper JSON-сообщений
     */
    public PaymentOrderEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Обрабатывает событие завершения заказа.
     *
     * @param payload JSON-сообщение из Kafka
     */
    @KafkaListener(topics = "${checkout.kafka.topics.order-completed}", groupId = "${spring.application.name}")
    public void handleOrderCompleted(String payload) {
        OrderCompletedEvent event = readEvent(payload);
        LOGGER.info(
            "Payment audit received order-completed event {} for order {} and payment {}",
            event.eventId(),
            event.orderId(),
            event.paymentId()
        );
    }

    private OrderCompletedEvent readEvent(String payload) {
        try {
            return objectMapper.readValue(payload, OrderCompletedEvent.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid order-completed event payload", exception);
        }
    }
}
