package org.gty.demo.config;

import com.google.common.base.Joiner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            var keyBuilder = new StringBuilder();
            keyBuilder.append(target.getClass().getName());
            keyBuilder.append(":");
            keyBuilder.append(method.getName());
            keyBuilder.append(":");

            var paramList = Arrays.stream(params)
                    .map(param -> param.getClass().getName() + ":" + String.valueOf(param))
                    .collect(Collectors.toList());

            return Joiner.on(":")
                    .appendTo(keyBuilder, paramList)
                    .toString();
        };
    }

}
