package org.gty.demo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            var keyBuilder = new StringBuilder();
            keyBuilder.append(target.getClass().getName());
            keyBuilder.append(".");
            keyBuilder.append(method.getName());

            var paramList = Arrays.stream(params)
                    .map(param -> {
                        var sb = new StringBuilder(" -> " + param.getClass().getName());
                        var temp = String.valueOf(param);

                        if (param instanceof String) {
                            sb.insert(0, "'" + temp + "'");
                        } else {
                            sb.insert(0, temp);
                        }

                        return sb.toString();
                    })
                    .collect(Collectors.toUnmodifiableList());

            var joiner = new StringJoiner(", ", "(", ")");
            paramList.forEach(joiner::add);

            keyBuilder.append(joiner.toString());

            return keyBuilder.toString();
        };
    }

}
