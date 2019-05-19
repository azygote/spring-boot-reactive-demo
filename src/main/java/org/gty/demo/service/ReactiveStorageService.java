package org.gty.demo.service;

import org.gty.demo.constant.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class ReactiveStorageService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveStorageService.class);

    private final StorageService storageService;

    @Autowired
    public ReactiveStorageService(@Nonnull StorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService, "storageService must not be null");
    }

    @Nonnull
    public Mono<Resource> loadAsResource(@Nonnull String filename) {
        Objects.requireNonNull(filename, "filename must not be null");

        return Mono.fromCallable(() -> storageService.loadAsResource(filename))
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }
}
