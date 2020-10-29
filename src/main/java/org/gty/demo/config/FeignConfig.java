package org.gty.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.http2client.Http2Client;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.reactive.ReactorFeign;
import org.gty.demo.config.properties.FeignProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class FeignConfig {

    @Bean
    public Feign.Builder feign(@Nonnull final FeignProperties properties,
                               @Nonnull final Gson gson,
                               @Nonnull final ObjectMapper mapper,
                               @Nonnull final Scheduler scheduler) {
        Objects.requireNonNull(properties, "properties must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(mapper, "mapper must not be null");
        Objects.requireNonNull(scheduler, "scheduler must not be null");

        Feign.Builder builder = ReactorFeign
            .builder()
            .scheduleOn(scheduler)
            .logLevel(properties.getLogLevel() != null ? properties.getLogLevel() : Logger.Level.NONE);

        final FeignProperties.FeignEncoder encoder = properties.getEncoder() != null ? properties.getEncoder() : FeignProperties.FeignEncoder.DEFAULT;
        switch (encoder) {
            case GSON -> builder = builder.encoder(new GsonEncoder(gson));
            case JACKSON -> builder = builder.encoder(new JacksonEncoder(mapper));
        }

        final FeignProperties.FeignDecoder decoder = properties.getDecoder() != null ? properties.getDecoder() : FeignProperties.FeignDecoder.DEFAULT;
        switch (decoder) {
            case GSON -> builder = builder.decoder(new GsonDecoder(gson));
            case JACKSON -> builder = builder.decoder(new JacksonDecoder(mapper));
        }

        final FeignProperties.FeignClient client = properties.getClient() != null ? properties.getClient() : FeignProperties.FeignClient.DEFAULT;
        switch (client) {
            case OKHTTP -> builder = builder.client(new OkHttpClient());
            case HTTP_2_CLIENT -> builder = builder.client(new Http2Client());
        }

        return builder;
    }
}
