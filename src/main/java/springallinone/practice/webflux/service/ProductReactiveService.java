package springallinone.practice.webflux.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import springallinone.practice.jpa.entity.Product;
import springallinone.practice.jpa.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductReactiveService {

    private final ProductRepository productRepository;

    public Flux<Product> findAll() {
        return Flux.defer(() -> Flux.fromIterable(productRepository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Product> findById(Long id) {
        return Mono.defer(() -> Mono.justOrEmpty(productRepository.findById(id)))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("product not found: " + id)));
    }
}
