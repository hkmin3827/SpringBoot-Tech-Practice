package springallinone.practice.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return sseEmitterService.subscribe(memberId);
    }

    @PostMapping("/notify/{memberId}")
    public ResponseEntity<Void> notify(
            @PathVariable Long memberId,
            @RequestParam String message
    ) {
        sseEmitterService.sendToMember(memberId, "notification", message);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcast(@RequestParam String message) {
        sseEmitterService.broadcast("broadcast", message);
        return ResponseEntity.noContent().build();
    }
}
