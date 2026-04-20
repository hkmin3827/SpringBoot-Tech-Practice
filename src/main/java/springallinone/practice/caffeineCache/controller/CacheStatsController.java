package springallinone.practice.caffeineCache.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Profile("caffeine")
@RestController
@RequestMapping("/cache/stats")
@RequiredArgsConstructor
public class CacheStatsController {

    private final CacheManager cacheManager;

    @GetMapping("/{cacheName}")
    public Map<String, Object> stats(@PathVariable String cacheName) {
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache(cacheName);
        if (cache == null) {
            return Map.of("error", "Cache not found" + cacheName);
        }

        CacheStats stats = cache.getNativeCache().stats();

        return Map.of(
                "hitRate", stats.hitRate(),
                "missRate", stats.missRate(),
                "hitCount", stats.hitCount(),
                "missCount", stats.missCount(),
                "evictionCount", stats.evictionCount(),
                "estimatedSize", cache.getNativeCache().estimatedSize()
        );
    }
}
