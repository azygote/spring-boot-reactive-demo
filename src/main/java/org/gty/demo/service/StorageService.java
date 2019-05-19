package org.gty.demo.service;

import org.springframework.core.io.Resource;

import javax.annotation.Nonnull;

public interface StorageService {

    @Nonnull
    Resource loadAsResource(@Nonnull String filename);
}
