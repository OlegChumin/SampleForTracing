package org.example.samplefortracing.inventory.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingScope;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Обрабатывает Kafka-события заказа в сервисе остатков.
 */
@Component
public class InventoryOrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryOrderEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final KafkaTracingService kafkaTracingService;

    /**
     * Создаёт consumer событий заказа.
     *
     * @param objectMapper mapper JSON-сообщений
     * @param kafkaTracingService сервис восстановления trace context из Kafka headers
     */
    public InventoryOrderEventConsumer(ObjectMapper objectMapper, KafkaTracingService kafkaTracingService) {
        this.objectMapper = objectMapper;
        this.kafkaTracingService = kafkaTracingService;
    }

    /**
     * Обрабатывает событие создания заказа.
     *
     * @param consumerRecord запись Kafka с payload и headers
     */
    @KafkaListener(topics = "${checkout.kafka.topics.order-created}", groupId = "${spring.application.name}")
    public void handleOrderCreated(ConsumerRecord<String, String> consumerRecord) {
        try (KafkaTracingScope ignored = kafkaTracingService.startConsumerSpan(
            consumerRecord,
            "kafka checkout.order-created inventory"
        )) {
            OrderCreatedEvent event = readEvent(consumerRecord.value());
            LOGGER.info(
                "Inventory audit received order-created event {} for order {} and item {}",
                event.eventId(),
                event.orderId(),
                event.itemId()
            );
        }
    }

    private OrderCreatedEvent readEvent(String payload) {
        try {
            return objectMapper.readValue(payload, OrderCreatedEvent.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid order-created event payload", exception);
        }
    }
}
