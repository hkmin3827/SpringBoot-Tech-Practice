package springallinone.practice.webflux.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import springallinone.practice.webflux.service.MemberReactiveService;

@Component
@RequiredArgsConstructor
public class MemberHandler {

    private final MemberReactiveService memberReactiveService;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return memberReactiveService.findAll()
                .collectList()
                .flatMap(members -> ServerResponse.ok().bodyValue(members));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return memberReactiveService.findById(id)
                .flatMap(member -> ServerResponse.ok().bodyValue(member))
                .onErrorResume(IllegalArgumentException.class, e -> ServerResponse.notFound().build());
    }
}
