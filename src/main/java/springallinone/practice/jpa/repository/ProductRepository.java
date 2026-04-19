package springallinone.practice.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springallinone.practice.jpa.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
