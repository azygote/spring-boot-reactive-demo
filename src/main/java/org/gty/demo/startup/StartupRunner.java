package org.gty.demo.startup;

import com.google.common.io.ByteStreams;
import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.entity.Test;
import org.gty.demo.repository.TestRepository;
import org.gty.demo.repository.r2dbc.ReactiveStudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Service
public class StartupRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    private final ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate;
    private final ReactiveStudentRepository reactiveStudentRepository;
    private final Scheduler scheduler;
    private final CountDownLatch latch = new CountDownLatch(2);
    private final TestRepository testRepository;

    public StartupRunner(@Nonnull ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate,
                         @Nonnull Scheduler scheduler,
                         @Nonnull ReactiveStudentRepository reactiveStudentRepository,
                         @Nonnull TestRepository testRepository) {
        this.reactiveRedisTemplate =
            Objects.requireNonNull(reactiveRedisTemplate, "redisTemplate must not be null");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must not be null");
        this.reactiveStudentRepository = Objects.requireNonNull(reactiveStudentRepository, "reactiveStudentRepository must not be null");
        this.testRepository = Objects.requireNonNull(testRepository, "testRepository must not be null");
    }

    private void flushRedis() {
        reactiveRedisTemplate.
            execute(connection -> connection.serverCommands().flushDb())
            .doOnComplete(() -> log.info("Successfully flushed redis."))
            .publishOn(scheduler)
            .doOnComplete(this::newFeature)
            .doOnComplete(latch::countDown)
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
    public void onReady(ApplicationReadyEvent event) throws InterruptedException {
        testHibernateInsert();
        testHibernateSelect();
        flushRedis();
        foobar();
        latch.await();
    }

    private void testHibernateInsert() {
        var test = new Test();

        Resource resource = new ClassPathResource("images/logo.png");

        try (var in = resource.getInputStream()) {
            test.setImg(ByteStreams.toByteArray(in));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }

        testRepository.saveAndFlush(test);
    }

    private void testHibernateSelect() {
        var test = testRepository.findAll();
        log.info("test = {}", test);
    }

    private void foobar() {
        log.info("foobar");

        reactiveStudentRepository
            .findAllByGenderAndDeleteFlag("Female", 1)
            .doOnNext(student -> log.info("foobar row = {}", student))
            .doOnComplete(latch::countDown)
            .subscribe();
    }
}
