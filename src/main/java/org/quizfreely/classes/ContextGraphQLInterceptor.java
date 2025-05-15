import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpCookie;

import org.quizfreely.classes.auth.AuthRepo;

@Component
public class ContextGraphQLInterceptor implements WebGraphQlInterceptor {
    @Autowired
    private AuthRepo authRepo;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        ServerWebExchange exchange = request.getExchange().get();

        String authToken;
        String authHeader = exchange.getRequest()
            .getHeaders().getFirst("Authorization");
        if (
            authHeader != null &&
            authHeader.substring(
                0, 7 /* "bearer " is first 7 characters */
            ).toLowerCase().equals("bearer ")
        ) {
            authToken = authHeader.substring(7);
        } else {
            HttpCookie authCookie = exchange.getRequest()
                .getCookies().getFirst("auth");
            if (authCookie != null) {
                authToken = authCookie.getValue();
            } else {
                authToken = null;
            }
        }

        request.configureExecutionInput((executionInput, builder) ->
            builder.graphQLContext(contextBuilder ->
                contextBuilder.of("authContext", authRepo.authContextUsingToken(authToken))
            ).build()
        );

        return chain.next(request);
    }
}

