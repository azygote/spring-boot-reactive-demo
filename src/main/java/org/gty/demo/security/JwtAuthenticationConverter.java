package org.gty.demo.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.lang3.StringUtils;
import org.gty.demo.config.properties.JwtProperties;
import org.gty.demo.constant.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Service
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtProperties jwtProperties;
    private final JWTVerifier jwtVerifier;

    public JwtAuthenticationConverter(
        @Nonnull final JwtProperties jwtProperties,
        @Nonnull final JWTVerifier jwtVerifier
    ) {
        this.jwtProperties = Objects.requireNonNull(jwtProperties, "[jwtProperties] must not be null");
        this.jwtVerifier = Objects.requireNonNull(jwtVerifier, "[jwtVerifier] must not be null");
    }

    @Override
    @Nonnull
    public Mono<Authentication> convert(@Nullable final ServerWebExchange exchange) {
        if (exchange == null) {
            return Mono.empty();
        }

        final var request = exchange.getRequest();
        final var authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        final var authorizationPrefix = jwtProperties.getAuthorizationPrefix();

        if (StringUtils.isBlank(authorization) ||
            !StringUtils.startsWithIgnoreCase(authorization, authorizationPrefix)) {
            return Mono.empty();
        }

        final var token = (authorization.length() <= authorizationPrefix.length()) ? StringUtils.EMPTY
            : authorization.substring(authorizationPrefix.length());

        try {
            final var jwt = jwtVerifier.verify(token);
            final var username = jwt.getClaim(JwtConstants.USERNAME).asString();
            return Mono.just(new JwtUsernameAuthenticationToken(username));
        } catch (final JWTVerificationException exception){
            return Mono.empty();
        }
    }
}
