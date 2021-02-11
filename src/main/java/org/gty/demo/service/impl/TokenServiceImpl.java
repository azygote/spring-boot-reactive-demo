package org.gty.demo.service.impl;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.gty.demo.config.properties.JwtProperties;
import org.gty.demo.constant.JwtConstants;
import org.gty.demo.exception.JwtAuthorizationException;
import org.gty.demo.model.form.TokenForm;
import org.gty.demo.model.vo.TokenVo;
import org.gty.demo.service.TokenService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Service
public class TokenServiceImpl implements TokenService {

    private final Scheduler scheduler;
    private final ReactiveAuthenticationManager authenticationManager;
    private final Algorithm algorithm;
    private final JWTCreator.Builder jwt;
    private final JwtProperties jwtProperties;

    public TokenServiceImpl(
        @Nonnull final Scheduler scheduler,
        @Nonnull final UserDetailsRepositoryReactiveAuthenticationManager userDetailsRepositoryReactiveAuthenticationManager,
        @Nonnull final Algorithm algorithm,
        @Nonnull final JWTCreator.Builder jwt,
        @Nonnull final JwtProperties jwtProperties
    ) {
        this.scheduler = Objects.requireNonNull(scheduler, "[scheduler] must not be null");
        authenticationManager = Objects.requireNonNull(userDetailsRepositoryReactiveAuthenticationManager, "[userDetailsRepositoryReactiveAuthenticationManager] must not be null");
        this.algorithm = Objects.requireNonNull(algorithm, "[algorithm] must not be null");
        this.jwt = Objects.requireNonNull(jwt, "[jwt] must not be null");
        this.jwtProperties = Objects.requireNonNull(jwtProperties, "[jwtProperties] must not be null");
    }

    @Nonnull
    @Override
    public Mono<TokenVo> issueToken(@Nonnull final TokenForm tokenForm) {
        Objects.requireNonNull(tokenForm, "[tokenForm] must not be null");

        final Authentication authentication =
            new UsernamePasswordAuthenticationToken(tokenForm.getUsername(), tokenForm.getPassword());

        return Mono.just(authentication)
            .publishOn(scheduler)
            .flatMap(authenticationManager::authenticate)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalStateException("No provider found for " + authentication.getClass()))))
            .map(postAuthenticatedAuthentication -> createToken(authentication))
            .map(tokenStr -> new TokenVo.Builder().withToken(tokenStr).build())
            .onErrorMap(JwtAuthorizationException::new)
            .subscribeOn(scheduler);
    }

    private String createToken(@Nonnull final Authentication authentication) {
        Objects.requireNonNull(authentication, "[authentication] must not be null");

        return jwt
            .withExpiresAt(Date.from(Instant.now().plus(jwtProperties.getExpiration())))
            .withClaim(JwtConstants.USERNAME, authentication.getName())
            .sign(algorithm);
    }
}
