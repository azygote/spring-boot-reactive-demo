package org.gty.demo.handler;

import org.gty.demo.model.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

final class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    static Mono<ServerResponse> renderErrorResponse(Mono<ServerResponse> response) {
        return response.onErrorResume(ex -> {
            log.warn(ex.toString(), ex);

            if (ex instanceof NullPointerException) {
                return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .syncBody(ResponseVo.illegalParameters(ex.toString()));
            } else if (ex instanceof IllegalArgumentException) {
                return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .syncBody(ResponseVo.illegalParameters(ex.toString()));
            } else {
                return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .syncBody(ResponseVo.internalError(ex.toString()));
            }
        });
    }

    private ExceptionHandler() {}
}
