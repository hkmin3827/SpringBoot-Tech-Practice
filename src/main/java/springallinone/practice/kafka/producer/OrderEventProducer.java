package springallinone.practice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import springallinone.practice.kafka.event.OrderCreatedEvent;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private static final String TOPIC = "order-created";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void pub(OrderCreatedEvent event) {
        CompletableFuture<SendResult<String, OrderCreatedEvent>> future = kafkaTemplate.send(TOPIC, String.valueOf(event.orderId()), event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Error while publishing order event: orderId= {}, msg= {}", event.orderId(), ex.getMessage());
            } else {
                log.info("Event published: topic={}, partition={}, offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
