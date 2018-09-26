package org.gty.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Objects;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory,
            ResourceLoader resourceLoader) {
        Objects.requireNonNull(reactiveRedisConnectionFactory,
                "reactiveRedisConnectionFactory must not be null");

        Objects.requireNonNull(resourceLoader, "resourceLoader must not be null");

        var genericToStringSerializer = new GenericToStringSerializer<>(Object.class);
        var jdkSerializer = new JdkSerializationRedisSerializer(Objects.requireNonNull(resourceLoader.getClassLoader()));

        var serializationContext = RedisSerializationContext
                .newSerializationContext()
                .key(genericToStringSerializer)
                .value(jdkSerializer)
                .hashKey(genericToStringSerializer)
                .hashValue(jdkSerializer)
                .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }
}
