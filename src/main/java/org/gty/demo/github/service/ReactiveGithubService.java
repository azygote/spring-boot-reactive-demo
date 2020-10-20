package org.gty.demo.github.service;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.slf4j.Slf4jLogger;
import org.gty.demo.github.model.Contributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

public interface ReactiveGithubService {

    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    Mono<Iterable<Contributor>> contributors(@Param("owner") @Nonnull final String owner,
                                             @Param("repo") @Nonnull final String repo);

    @Configuration(proxyBeanMethods = false)
    class ReactiveGithubServiceConfig {

        private static final String GITHUB_BASE_URL = "https://api.github.com";

        @Nonnull
        @Bean
        public ReactiveGithubService githubService(@Nonnull final Feign.Builder feign) {
            Objects.requireNonNull(feign, "feign must not be null");

            return feign
                .logger(new Slf4jLogger(ReactiveGithubService.class))
                .target(ReactiveGithubService.class, GITHUB_BASE_URL);
        }
    }
}
