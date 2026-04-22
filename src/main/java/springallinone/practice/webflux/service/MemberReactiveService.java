package springallinone.practice.webflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import springallinone.practice.jpa.entity.Member;
import springallinone.practice.jpa.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberReactiveService {

    private final MemberRepository memberRepository;

    public Flux<Member> findAll() {
        return Flux.defer(() -> Flux.fromIterable(memberRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Member> findById(Long id) {
        return Mono.defer(() -> Mono.justOrEmpty(memberRepository.findById(id)))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("member not found: " + id)));
    }
}
