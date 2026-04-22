package springallinone.practice.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import springallinone.practice.elasticsearch.document.ProductDocument;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {

    List<ProductDocument> findByName(String name);

    List<ProductDocument> findByNameContaining(String name);

    List<ProductDocument> findByCategory(String category);
}
