package org.example.samplefortracing.payment.event;

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
 * Обрабатывает Kafka-события заказа в платёжном сервисе.
 */
@Component
public class PaymentOrderEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentOrderEventConsumer.class);

    private final ObjectMapper objectMapper;
    private final KafkaTracingService kafkaTracingService;

    /**
     * Создаёт consumer событий заказа.
     *
     * @param objectMapper mapper JSON-сообщений
     * @param kafkaTracingService сервис восстановления trace context из Kafka headers
     */
    public PaymentOrderEventConsumer(ObjectMapper objectMapper, KafkaTracingService kafkaTracingService) {
        this.objectMapper = objectMapper;
        this.kafkaTracingService = kafkaTracingService;
    }

    /**
     * Обрабатывает событие завершения заказа.
     *
     * @param consumerRecord запись Kafka с payload и headers
     */
    @KafkaListener(topics = "${checkout.kafka.topics.order-completed}", groupId = "${spring.application.name}")
    public void handleOrderCompleted(ConsumerRecord<String, String> consumerRecord) {
        try (KafkaTracingScope ignored = kafkaTracingService.startConsumerSpan(
            consumerRecord,
            "kafka checkout.order-completed payment"
        )) {
            OrderCompletedEvent event = readEvent(consumerRecord.value());
            LOGGER.info(
                "Payment audit received order-completed event {} for order {} and payment {}",
                event.eventId(),
                event.orderId(),
                event.paymentId()
            );
        }
    }

    private OrderCompletedEvent readEvent(String payload) {
        try {
            return objectMapper.readValue(payload, OrderCompletedEvent.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid order-completed event payload", exception);
        }
    }
}
