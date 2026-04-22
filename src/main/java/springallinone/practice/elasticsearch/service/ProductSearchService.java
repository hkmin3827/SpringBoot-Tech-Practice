package springallinone.practice.elasticsearch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import springallinone.practice.elasticsearch.document.ProductDocument;
import springallinone.practice.elasticsearch.repository.ProductSearchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public ProductDocument save(ProductDocument document) {
        return productSearchRepository.save(document);
    }

    public List<ProductDocument> searchByName(String name) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("name").query(name)))
                .build();

        return elasticsearchOperations.search(query, ProductDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public List<ProductDocument> multiMatchSearch(String keyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m
                        .fields("name", "category")
                        .query(keyword)))
                .build();

        return elasticsearchOperations.search(query, ProductDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public List<ProductDocument> fuzzySearch(String keyword) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.fuzzy(f -> f
                        .field("name")
                        .value(keyword)
                        .fuzziness("AUTO")))
                .build();

        return elasticsearchOperations.search(query, ProductDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
