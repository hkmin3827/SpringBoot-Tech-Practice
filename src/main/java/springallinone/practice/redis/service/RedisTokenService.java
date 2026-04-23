package springallinone.practice.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(Long memberId, String token, Duration ttl) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + memberId, token, ttl);
    }

    public Optional<String> getRefreshToken(Long memberId) {
        Object token =  redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + memberId);
        return Optional.ofNullable((String) token);
    }

    public void deleteRefreshToken(Long memberId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + memberId);
    }

    public void blacklistToken(String token, Duration ttl) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, token, ttl);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}
