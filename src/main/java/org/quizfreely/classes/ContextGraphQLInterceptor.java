import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpCookie;
import org.quizfreely.classes.auth.AuthRepo;

@Component
public class ContextGraphQLInterceptor implements WebGraphQlInterceptor {
    @Autowired
    private AuthRepo authRepo;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String authToken = null;

        /* try getting "Authorization" header */
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (
            authHeader != null &&
            authHeader.substring(
                0, 7 /* "bearer " is first 7 characters */
            ).toLowerCase().equals("bearer ")
        ) {
            authToken = authHeader.substring(7);
        } else {
            /* try getting "auth" cookie */
            HttpCookie authCookie = request.getCookies().getFirst("auth");
            if (authCookie != null) {
                authToken = authCookie.getValue();
            }
        }

        String finalAuthToken = authToken;
        request.configureExecutionInput((executionInput, builder) ->
            builder.graphQLContext(contextBuilder ->
                contextBuilder.of("authContext", authRepo.authContextUsingToken(finalAuthToken))
            ).build()
        );

        return chain.next(request);
    }
}

