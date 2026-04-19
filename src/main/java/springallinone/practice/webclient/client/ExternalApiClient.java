package springallinone.practice.webclient.client;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import springallinone.practice.webclient.exception.*;

@Component
@RequiredArgsConstructor
public class ExternalApiClient {

    private final WebClient webClient;

    public <T> Mono<T> get(String baseUrl, String path, Class<T> responseType) {
        return applyErrorHandling(
                webClient.get().uri(baseUrl + path).retrieve()
        ).bodyToMono(responseType);
    }

    public <T> Mono<T> post(String baseUrl, String path, Object requestBody, Class<T> responseType) {
        return applyErrorHandling(
                webClient.post().uri(baseUrl + path).bodyValue(requestBody).retrieve()
        ).bodyToMono(responseType);
    }

    private WebClient.ResponseSpec applyErrorHandling(WebClient.ResponseSpec spec) {
        return spec
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(handleClientError(response.statusCode(), body))))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ExternalServerException(body))));
    }

    private RuntimeException handleClientError(HttpStatusCode statusCode, String body) {
        if (statusCode == HttpStatus.BAD_REQUEST)      return new BadRequestException(body);
        if (statusCode == HttpStatus.UNAUTHORIZED)     return new UnauthorizedException(body);
        if (statusCode == HttpStatus.FORBIDDEN)        return new ForbiddenException(body);
        if (statusCode == HttpStatus.NOT_FOUND)        return new NotFoundException(body);
        if (statusCode == HttpStatus.TOO_MANY_REQUESTS) return new TooManyRequestsException(body);
        return new IllegalArgumentException("Unhandled client error " + statusCode + ": " + body);
    }
}
