package org.gty.demo.security;

import com.google.gson.Gson;
import org.gty.demo.model.vo.ResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class JwtServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final Gson gson;

    public JwtServerAuthenticationEntryPoint(@Nonnull final Gson gson) {
        this.gson = Objects.requireNonNull(gson, "[gson] must not be null");
    }

    @Override
    public Mono<Void> commence(
        final ServerWebExchange exchange,
        final AuthenticationException ex
    ) {
        final var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        final var responseVo = ResponseVo.unauthorized(ex.toString());
        final var responseVoJson = gson.toJson(responseVo);
        final var bytes = responseVoJson.getBytes(StandardCharsets.UTF_8);
        final var buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
