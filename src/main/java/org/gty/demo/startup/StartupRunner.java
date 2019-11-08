package org.gty.demo.startup;

import org.gty.demo.constant.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
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
            .doOnComplete(this::newFeature)
            .subscribe();
    }

    private void newFeature() {
        var logInfo = """
        This is log information.
        Breaks in multiple lines.
        """;

        log.info(logInfo);

        var dateTime = ZonedDateTime.now(SystemConstants.defaultTimeZone);
        var monthInfo = "This is " + switch (dateTime.getMonth()) {
            case JANUARY -> "JAN";
            case FEBRUARY -> "FEB";
            case MARCH -> "MAR";
            case APRIL -> "APR";
            case MAY -> "MAY";
            case JUNE -> "JUN";
            case JULY -> "JUL";
            case AUGUST -> "AUG";
            case SEPTEMBER -> "SEPT";
            case OCTOBER -> "OCT";
            case NOVEMBER -> "NOV";
            default -> "DEC";
        };

        log.info(monthInfo);
    }

    @EventListener
    public void onReady(ApplicationReadyEvent event) {
        flushRedis();
    }
}
