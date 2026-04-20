package springallinone.practice.cache.dto;

import springallinone.practice.jpa.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        int quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
