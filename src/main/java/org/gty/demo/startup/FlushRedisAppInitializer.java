package org.gty.demo.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
@OrderedAppInitializer
@Order(OrderedAppInitializer.ORDER_FLUSH_REDIS)
public final class FlushRedisAppInitializer implements AppInitializer {

    private static final Logger log = LoggerFactory.getLogger(FlushRedisAppInitializer.class);

    private final ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate;
    private final Scheduler scheduler;

    public FlushRedisAppInitializer(@Nonnull final ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate,
                                    @Nonnull final Scheduler scheduler) {
        this.reactiveRedisTemplate =
            Objects.requireNonNull(reactiveRedisTemplate, "redisTemplate must not be null");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must not be null");
    }

    @Override
    public void onAppInitialize() {
        reactiveRedisTemplate.
            execute(connection -> connection.serverCommands().flushDb())
            .publishOn(scheduler)
            .doOnComplete(() -> log.info("Successfully flushed redis."))
            .subscribe();
    }
}
