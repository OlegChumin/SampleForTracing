package org.example.samplefortracing.order.event;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.nextbi.dataflow.tracing.common.kafka.KafkaTracingService;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Проверяет публикацию checkout-событий в Kafka.
 */
class KafkaCheckoutEventPublisherTests {

    private final KafkaTemplate<String, Object> kafkaTemplate = mock(KafkaTemplate.class);
    private final KafkaTracingService kafkaTracingService = mock(KafkaTracingService.class);
    private final KafkaCheckoutEventPublisher publisher = new KafkaCheckoutEventPublisher(
        kafkaTemplate,
        kafkaTracingService,
        "checkout.order-created",
        "checkout.order-completed"
    );

    /**
     * Проверяет отправку события создания заказа.
     */
    @Test
    void publishOrderCreatedSendsEventToKafka() {
        OrderCreatedEvent event = new OrderCreatedEvent("EVT-1", "ORD-1", "customer-1", "SKU-1", 2);
        when(kafkaTemplate.send(org.mockito.ArgumentMatchers.<ProducerRecord<String, Object>>any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        publisher.publishOrderCreated(event);

        ArgumentCaptor<ProducerRecord<String, Object>> captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaTracingService).injectTraceContext(captor.capture());
        verify(kafkaTemplate).send(captor.getValue());
    }

    /**
     * Проверяет отправку события завершения заказа.
     */
    @Test
    void publishOrderCompletedSendsEventToKafka() {
        OrderCompletedEvent event = new OrderCompletedEvent(
            "EVT-2",
            "ORD-1",
            "customer-1",
            new BigDecimal("268.80"),
            "USD",
            "PAY-1",
            "COMPLETED"
        );
        when(kafkaTemplate.send(org.mockito.ArgumentMatchers.<ProducerRecord<String, Object>>any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        publisher.publishOrderCompleted(event);

        ArgumentCaptor<ProducerRecord<String, Object>> captor = ArgumentCaptor.forClass(ProducerRecord.class);
        verify(kafkaTracingService).injectTraceContext(captor.capture());
        verify(kafkaTemplate).send(captor.getValue());
    }
}
