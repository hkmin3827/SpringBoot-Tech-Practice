package springallinone.practice.redis.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import springallinone.practice.redis.config.RedisConfig;

@Component
@RequiredArgsConstructor
public class RedisMessagePublisher{

    private final StringRedisTemplate stringRedisTemplate;

    public void publish(String message) {
        stringRedisTemplate.convertAndSend(RedisConfig.NOTIFICATION_CHANNEL, message);
    }

    public void publishTo(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
