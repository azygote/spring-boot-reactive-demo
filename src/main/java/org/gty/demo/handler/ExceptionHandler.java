package org.gty.demo.handler;

import org.gty.demo.model.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

final class ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    static Mono<ResponseVo<?>> renderErrorResponse(@Nonnull Throwable ex) {
        Objects.requireNonNull(ex, "ex must not be null");

        log.warn(ex.toString(), ex);

        ResponseVo<String> temp;

        if (ex instanceof NullPointerException
                || ex instanceof IllegalArgumentException) {
            temp = ResponseVo.illegalParameters(ex.toString());
        } else {
            temp = ResponseVo.internalError(ex.toString());
        }

        return Mono.just(temp);
    }

    private ExceptionHandler() {}
}
