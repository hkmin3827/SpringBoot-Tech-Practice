package springallinone.practice.elasticsearch.config;

import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.Jackson3JsonpMapper;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Configuration
@NullMarked
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    private final String elasticsearchUri;
    private final JsonMapper objectMapper;

    public ElasticsearchConfig(
            @Value("${spring.elasticsearch.uris}") String elasticsearchUri,
            JsonMapper objectMapper) {
        this.elasticsearchUri = elasticsearchUri;
        this.objectMapper = objectMapper;
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        String cleanUri = elasticsearchUri
                .replace("http://", "")
                .replace("https://", "");

        return ClientConfiguration.builder()
                .connectedTo(cleanUri)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(30))
                .build();
    }

    @Override
    public JsonpMapper jsonpMapper() {
        return new Jackson3JsonpMapper(objectMapper);
    }
}
