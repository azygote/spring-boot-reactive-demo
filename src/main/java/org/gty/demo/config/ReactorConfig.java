package org.gty.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;

@Configuration
public class ReactorConfig {

    @Bean
    @Nonnull
    public Scheduler defaultReactorScheduler(@Nonnull final ThreadPoolTaskExecutor asyncExecutor) {
        return Schedulers.fromExecutor(asyncExecutor);
    }
}
