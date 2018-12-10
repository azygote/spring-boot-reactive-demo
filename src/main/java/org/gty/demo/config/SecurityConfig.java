package org.gty.demo.config;

import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                    .matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .pathMatchers(generateRestfulAntPathMatchers("/api/student")).hasRole("USER")
                    .pathMatchers(generateRestfulAntPathMatchers("/api/actuator")).hasRole("ACTUATOR")
                .anyExchange().authenticated().and()
                .httpBasic().and()
                .exceptionHandling().authenticationEntryPoint((var exchange, var e) -> {
                    var response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

                    var responseVo = ResponseVo.unauthorized(e.toString());
                    var responseVoJson = JsonUtils.toJson(responseVo);
                    var bytes = responseVoJson.getBytes(StandardCharsets.UTF_8);
                    var buffer = response.bufferFactory().wrap(bytes);
                    return response.writeWith(Mono.just(buffer));
                }).and()
                .build();
    }

    @Nonnull
    private static String[] generateRestfulAntPathMatchers(@Nonnull String url) {
        Objects.requireNonNull(url, "url must not be null");

        return new String[] {url, url + "/**"};
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
