package org.gty.demo.config;

import org.gty.demo.security.JwtAuthenticationFilter;
import org.gty.demo.security.JwtServerAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

import javax.annotation.Nonnull;
import java.util.Objects;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    @Bean
    @Nonnull
    public SecurityWebFilterChain springSecurityFilterChain(
        @Nonnull final ServerHttpSecurity http,
        @Nonnull final JwtAuthenticationFilter jwtAuthenticationFilter,
        @Nonnull final JwtServerAuthenticationEntryPoint jwtServerAuthenticationEntryPoint
    ) {
        Objects.requireNonNull(http, "http must not be null");
        Objects.requireNonNull(jwtAuthenticationFilter, "[jwtAuthenticationFilter] must not be null");
        Objects.requireNonNull(jwtServerAuthenticationEntryPoint, "[jwtServerAuthenticationEntryPoint] must not be null");

        return http
            .csrf().disable()
            .logout().disable()
            .requestCache().requestCache(NoOpServerRequestCache.getInstance()).and()
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange()
                .matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .pathMatchers(generateRestfulAntPathMatchers("/api/token")).permitAll()
                .pathMatchers(generateRestfulAntPathMatchers("/api/student")).hasRole("USER")
                .pathMatchers(generateRestfulAntPathMatchers("/api/actuator")).hasRole("ACTUATOR")
                .pathMatchers(generateRestfulAntPathMatchers("/api/files")).hasRole("USER")
                .pathMatchers(generateRestfulAntPathMatchers("/api/github")).hasRole("USER")
            .anyExchange().authenticated().and()
            .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC)
            .exceptionHandling().authenticationEntryPoint(jwtServerAuthenticationEntryPoint).and()
            .build();
    }

    @Nonnull
    private static String[] generateRestfulAntPathMatchers(@Nonnull final String url) {
        Objects.requireNonNull(url, "url must not be null");

        return new String[]{url, url + "/**"};
    }

    @Bean
    @Nonnull
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
