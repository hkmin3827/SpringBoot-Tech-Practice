package springallinone.practice.webflux.handler;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import springallinone.practice.jpa.repository.MemberRepository;

@CommandNaming
@RequiredArgsConstructor
public class MemberHandler {

    private final MemberRepository  memberRepository;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return Flux.defer(() -> Flux.fromIterable(memberRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic())
                .collectList()
                .flatMap(members -> ServerResponse.ok().bodyValue(members));
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return Mono.defer(() -> Mono.justOrEmpty(memberRepository.findById(id)))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(member -> ServerResponse.ok().bodyValue(member))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
