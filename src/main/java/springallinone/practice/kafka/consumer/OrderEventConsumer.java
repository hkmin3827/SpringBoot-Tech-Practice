package springallinone.practice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import springallinone.practice.kafka.event.OrderCreatedEvent;

@Component
@Slf4j
public class OrderEventConsumer {

    @RetryableTopic(
            attempts = "3",
            backOff = @BackOff(delay = 1000, multiplier = 2.0),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(
            topics = "order-created",
            groupId = "order-group",
            containerFactory = "orderKafkaListenerContainerFactory"
    )
    public void consume(
            @Payload OrderCreatedEvent event,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment ack
            ) {
        log.info("Received OrderCreatedEvent: orderId= {}", event.orderId());
        ack.acknowledge();
    }

    @KafkaListener(topics = "order-created-dlt", groupId = "order-group-dlt")
    public void consumeDeadLetter(@Payload OrderCreatedEvent event) {
        log.error("Dead letter received: orderId= {}", event.orderId());
    }
}
