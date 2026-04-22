package springallinone.practice.webflux.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springallinone.practice.jpa.entity.Product;
import springallinone.practice.webflux.service.ProductReactiveService;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactive/products")
public class ProductReactiveController {

    private final ProductReactiveService  productReactiveService;

    @GetMapping
    public Flux<Product> findAll() {
        return productReactiveService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Product> findById(@PathVariable Long id) {
        return productReactiveService.findById(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> findAllStream() {
        return productReactiveService.findAll()
                .delayElements(Duration.ofMillis(500));
    }
}
