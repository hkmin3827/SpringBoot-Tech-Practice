package springallinone.practice.webflux.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import springallinone.practice.webflux.handler.MemberHandler;

@Configuration
public class MemberRouter {

    @Bean
    RouterFunction<ServerResponse> memberRouterFunction(MemberHandler memberHandler) {
        return RouterFunctions.route()
                .GET("/api/functional/members", memberHandler::findAll)
                .GET("/api/functional/members/{id}", memberHandler::findById)
                .build();
    }
}
