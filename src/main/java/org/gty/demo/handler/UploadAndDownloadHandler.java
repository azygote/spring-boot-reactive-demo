package org.gty.demo.handler;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.service.ReactiveStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class UploadAndDownloadHandler {

    private static final Logger log = LoggerFactory.getLogger(UploadAndDownloadHandler.class);

    private final ReactiveStorageService storageService;

    @Autowired
    public UploadAndDownloadHandler(@Nonnull ReactiveStorageService storageService) {
        this.storageService = Objects.requireNonNull(storageService, "storageService must not be null");
    }

    @Nonnull
    public Mono<ServerResponse> download(@Nonnull ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var filenameMono = Mono.just(request.pathVariable("filename"))
                .map(filename -> new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

        var resourceMono = filenameMono.flatMap(storageService::loadAsResource);

        var response = Mono.zip(filenameMono, resourceMono)
                .flatMap(tuple2 -> {
                    var filename = tuple2.getT1();
                    var resource = tuple2.getT2();

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header(HttpHeaders.CONTENT_DISPOSITION, filename)
                            .syncBody(resource)
                            .subscribeOn(SystemConstants.defaultReactorScheduler());
                });

        return renderErrorResponse(response);
    }

    @Nonnull
    public Mono<ServerResponse> upload(@Nonnull ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var response = request.body(BodyExtractors.toMultipartData())
                .publishOn(SystemConstants.defaultReactorScheduler())
                .doOnSuccess(parts -> log.info("{}", parts))
                .<ResponseVo<?>>thenReturn(ResponseVo.success())
                .flatMap(t -> ServerResponse.ok().syncBody(t).subscribeOn(SystemConstants.defaultReactorScheduler()));

        return renderErrorResponse(response);
    }

    @Nonnull
    private static Mono<ServerResponse> renderErrorResponse(@Nonnull Mono<ServerResponse> response) {
        return Objects.requireNonNull(response, "response must not be null")
                .onErrorResume(ex -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(ExceptionHandler.renderErrorResponse(ex),
                                new ParameterizedTypeReference<ResponseVo<?>>() {
                                })
                        .subscribeOn(SystemConstants.defaultReactorScheduler()));
    }
}
