package org.gty.demo.config;

import com.google.common.base.Suppliers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@Configuration
@EnableAsync
public class AsyncConfig {

    private static final int processorsCount;
    private static final Supplier<Integer> poolSize;
    private static final int DEFAULT_POOL_SIZE = 600;

    private static int getPoolSize() {
        return poolSize.get();
    }

    static {
        processorsCount = Runtime.getRuntime().availableProcessors();
        poolSize = Suppliers.memoize(() -> DEFAULT_POOL_SIZE * 2);
    }

    @Bean(destroyMethod = "shutdown")
    @Nonnull
    public ThreadPoolTaskExecutor asyncExecutor() {
        final var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(getPoolSize());
        executor.setMaxPoolSize(1500);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("sbrd-default-async-executor-");
        executor.initialize();
        return executor;
    }
}
