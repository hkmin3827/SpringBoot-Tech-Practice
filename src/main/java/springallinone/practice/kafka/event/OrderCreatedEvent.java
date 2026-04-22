package springallinone.practice.kafka.event;

import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        Long memberId,
        Long productId,
        int quantity,
        LocalDateTime createdAt
) {
}
