package org.gty.demo.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class StartupRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    private ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate;

    public StartupRunner(@Nonnull ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate) {
        this.reactiveRedisTemplate =
                Objects.requireNonNull(reactiveRedisTemplate, "redisTemplate must not be null");
    }

    private void flushRedis() {
        reactiveRedisTemplate.
                execute(connection -> connection
                        .serverCommands()
                        .flushDb())
                .doOnComplete(() -> log.info("Successfully flushed redis."))
                .subscribe();
    }

    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        flushRedis();
    }
}
