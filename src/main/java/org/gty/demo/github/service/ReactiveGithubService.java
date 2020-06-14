package org.gty.demo.github.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.reactive.ReactorFeign;
import feign.slf4j.Slf4jLogger;
import org.gty.demo.config.properties.FeignLoggingProperties;
import org.gty.demo.github.model.Contributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public interface ReactiveGithubService {

    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    Mono<List<Contributor>> contributors(@Param("owner") @Nonnull final String owner,
                                         @Param("repo") @Nonnull final String repo);

    @Configuration
    class ReactiveGithubServiceConfig {

        private static final String GITHUB_BASE_URL = "https://api.github.com";

        @Nonnull
        @Bean
        public ReactiveGithubService githubService(@Nonnull final FeignLoggingProperties properties,
                                                   @Nonnull final ObjectMapper mapper,
                                                   @Nonnull final Scheduler scheduler) {
            Objects.requireNonNull(properties, "properties must not be null");
            Objects.requireNonNull(mapper, "mapper must not be null");
            Objects.requireNonNull(scheduler, "scheduler must not be null");

            return ReactorFeign
                .builder()
                .scheduleOn(scheduler)
                .logger(new Slf4jLogger(ReactiveGithubService.class))
                .logLevel(properties.getLevel())
                .encoder(new JacksonEncoder(mapper))
                .decoder(new JacksonDecoder(mapper))
                .client(new OkHttpClient())
                .target(ReactiveGithubService.class, GITHUB_BASE_URL);
        }
    }
}
