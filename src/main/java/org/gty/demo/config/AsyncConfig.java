package org.gty.demo.config;

import com.google.common.base.Suppliers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.function.Supplier;

@Configuration
@EnableAsync
public class AsyncConfig {

    private static final int processorsCount;
    private static final Supplier<Integer> poolSize;

    private static int getPoolSize() {
        return poolSize.get();
    }

    static {
        processorsCount = Runtime.getRuntime().availableProcessors();
        poolSize = Suppliers.memoize(() -> 600);
    }

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor asyncExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getPoolSize());
        executor.setMaxPoolSize(1000);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("sbrd-default-async-executor-");
        executor.initialize();
        return executor;
    }
}
