package org.gty.demo.constant;

import com.google.common.base.Suppliers;
import org.gty.demo.util.SpringBeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class SystemConstants {

    public static final ZoneId defaultTimeZone = ZoneId.systemDefault();
    public static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSZZZ");

    private static final Supplier<Scheduler> defaultReactorSchedulerSupplier;

    static {
        defaultReactorSchedulerSupplier = Suppliers.memoize(() -> {
           var executor = SpringBeanUtils.getBean("asyncExecutor", ThreadPoolTaskExecutor.class);
           return Schedulers.fromExecutor(executor);
        });
    }

    public static Scheduler defaultReactorScheduler() {
        return defaultReactorSchedulerSupplier.get();
    }

    private SystemConstants() {
    }
}
