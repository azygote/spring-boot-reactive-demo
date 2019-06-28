package org.gty.demo.controller;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.form.UploadFormData;
import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.service.ReactiveStorageService;
import org.gty.demo.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public final class UploadAndDownloadController {

    private static final Logger log = LoggerFactory.getLogger(UploadAndDownloadController.class);

    private final ReactiveStorageService storageService;

    public UploadAndDownloadController(@Nonnull ReactiveStorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService, "storageService must not be null");
    }

    @GetMapping(value = "/api/files/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Mono<Resource>> download(@Nonnull @PathVariable("filename") final String filename) {
        final var resourceMono = Mono
            .just(Objects.requireNonNull(filename, "filename must not be null"))
            .flatMap(storageService::loadAsResource)
            .subscribeOn(SystemConstants.defaultReactorScheduler());

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1))
            .body(resourceMono);
    }

    @PostMapping(value = "/api/files/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseVo<Void>> upload(@Nonnull final Mono<UploadFormData> formDataMono) {
        return Objects.requireNonNull(formDataMono, "formDataMono must not be null")
            .doOnSuccess(ValidationUtils::validate)
            .doOnSuccess(form -> log.debug("{}", form))
            .thenReturn(ResponseVo.<Void>success())
            .subscribeOn(SystemConstants.defaultReactorScheduler());
    }
}
