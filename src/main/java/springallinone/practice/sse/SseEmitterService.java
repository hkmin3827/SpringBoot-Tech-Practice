package springallinone.practice.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseEmitterService {

    private static final long TIMEOUT_MS = 60 * 1000L;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_MS);

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> {
            emitters.remove(memberId);
            emitter.complete();
        });
        emitter.onError(e -> emitters.remove(memberId));

        emitters.put(memberId, emitter);

        try {
            emitter.send(SseEmitter.event().name("connect").data("connencted"));
        } catch (IOException e) {
            emitters.remove(memberId);
        }

        return emitter;
    }

    public void sendToMember(Long memberId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter == null) {
            return;
        }
        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException e) {
            emitters.remove(memberId);
            log.warn("SSE send failed - memberId = {}, removed emitter", memberId);
        }
    }

    public void broadcast(String eventName, Object data) {
        emitters.forEach((memberId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        });
    }
}
