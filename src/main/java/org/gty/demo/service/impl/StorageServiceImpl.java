package org.gty.demo.service.impl;

import org.gty.demo.config.properties.SbrdProperties;
import org.gty.demo.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

    private final SbrdProperties sbrdProperties;

    public StorageServiceImpl(@Nonnull SbrdProperties sbrdProperties) {
        this.sbrdProperties = Objects.requireNonNull(sbrdProperties, "sbrdProperties must not be null");
    }

    @Nonnull
    @Override
    public Resource loadAsResource(@Nonnull String filename) {
        Objects.requireNonNull(filename, "filename must not be null");

        var bytesContent = sbrdProperties.getStorageContent().getBytes(StandardCharsets.UTF_8);

        try (var contentInputStream = new ByteArrayInputStream(bytesContent)) {
            return new InputStreamResource(contentInputStream);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
