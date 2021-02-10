package org.gty.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class JwtAuthenticationFilter implements WebFilter  {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final ReactiveAuthenticationManagerResolver<ServerWebExchange> authenticationManagerResolver;
    private final ServerAuthenticationConverter authenticationConverter;
    private final ServerWebExchangeMatcher requiresAuthenticationMatcher = ServerWebExchangeMatchers.anyExchange();
    private final ServerSecurityContextRepository securityContextRepository =
        NoOpServerSecurityContextRepository.getInstance();
    private final ServerAuthenticationSuccessHandler authenticationSuccessHandler =
        new WebFilterChainServerAuthenticationSuccessHandler();
    private final ServerAuthenticationFailureHandler authenticationFailureHandler;

    public JwtAuthenticationFilter(@Nonnull final JwtAuthenticationManager jwtAuthenticationManager,
                                   @Nonnull final JwtAuthenticationConverter jwtAuthenticationConverter,
                                   @Nonnull final JwtServerAuthenticationEntryPoint jwtServerAuthenticationEntryPoint) {
        authenticationManagerResolver = request -> Mono.just(
            Objects.requireNonNull(jwtAuthenticationManager, "[jwtAuthenticationManager] must not be null")
        );
        authenticationConverter = Objects.requireNonNull(
            jwtAuthenticationConverter,
            "[jwtAuthenticationConverter] must not be null"
        );
        authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(
            Objects.requireNonNull(
                jwtServerAuthenticationEntryPoint,
                "[jwtServerAuthenticationEntryPoint] must not be null"
            )
        );
    }

    @Nonnull
    @Override
    public Mono<Void> filter(@Nonnull final ServerWebExchange exchange, @Nonnull final WebFilterChain chain) {
        Objects.requireNonNull(exchange, "[exchange] must not be null");
        Objects.requireNonNull(chain, "[chain] must not be null");


        return requiresAuthenticationMatcher.matches(exchange).filter(ServerWebExchangeMatcher.MatchResult::isMatch)
            .flatMap(matchResult -> authenticationConverter.convert(exchange))
            .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
            .flatMap(token -> authenticate(exchange, chain, token))
            .onErrorResume(AuthenticationException.class,
                ex -> authenticationFailureHandler.onAuthenticationFailure(new WebFilterExchange(exchange, chain), ex));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain, Authentication token) {
        return authenticationManagerResolver.resolve(exchange)
            .flatMap(authenticationManager -> authenticationManager.authenticate(token))
            .switchIfEmpty(Mono.defer(
                () -> Mono.error(new IllegalStateException("No provider found for " + token.getClass()))))
            .flatMap(authentication -> onAuthenticationSuccess(authentication,
                new WebFilterExchange(exchange, chain)))
            .doOnError(AuthenticationException.class,
                ex -> logger.debug("Authentication failed: {}", ex.getMessage()));
    }

    private Mono<Void> onAuthenticationSuccess(final Authentication authentication, final WebFilterExchange webFilterExchange) {
        final ServerWebExchange exchange = webFilterExchange.getExchange();
        final SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return securityContextRepository.save(exchange, securityContext)
            .then(authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication))
            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }
}
