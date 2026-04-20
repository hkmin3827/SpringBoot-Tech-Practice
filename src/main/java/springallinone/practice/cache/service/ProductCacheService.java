package springallinone.practice.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springallinone.practice.cache.dto.ProductResponse;
import springallinone.practice.jpa.entity.Product;
import springallinone.practice.jpa.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCacheService {

    private final ProductRepository productRepository;

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
    }

    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream().map(ProductResponse::from)
                .toList();
    }

    @CachePut(value = "products", key = "#result.id")
    @Transactional
    public ProductResponse save(Product product) {
        Product savedProduct = productRepository.save(product);
        return ProductResponse.from(savedProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void clearAll() {}
}
