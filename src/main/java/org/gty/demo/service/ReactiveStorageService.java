package org.gty.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class ReactiveStorageService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveStorageService.class);

    private final StorageService storageService;
    private final Scheduler scheduler;

    public ReactiveStorageService(@Nonnull final StorageService storageService,
                                  @Nonnull final Scheduler scheduler) {
        this.storageService = Objects.requireNonNull(storageService, "storageService must not be null");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must not be null");
    }

    @Nonnull
    public Mono<Resource> loadAsResource(@Nonnull String filename) {
        Objects.requireNonNull(filename, "filename must not be null");

        return Mono.fromCallable(() -> storageService.loadAsResource(filename))
            .subscribeOn(scheduler);
    }
}
