package org.gty.demo.controller;

import org.gty.demo.model.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Nonnull;
import java.util.Objects;

@RestControllerAdvice("org.gty.demo.controller")
public final class ExceptionControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NullPointerException.class)
    @Nonnull
    public ResponseEntity<ResponseVo<String>> handleNullPointerException(@Nonnull final NullPointerException ex) {
        return illegalParameters(Objects.requireNonNull(ex, "ex must not be null"));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    @Nonnull
    public ResponseEntity<ResponseVo<String>> handleIllegalArgumentException(@Nonnull final IllegalArgumentException ex) {
        return illegalParameters(Objects.requireNonNull(ex, "ex must not be null"));
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    @Nonnull
    public ResponseEntity<ResponseVo<String>> handleException(@Nonnull final Exception ex) {
        return internalError(Objects.requireNonNull(ex, "ex must not be null"));
    }

    @Nonnull
    private <T> ResponseEntity<ResponseVo<T>> error(@Nonnull final Exception ex,
                                                    @Nonnull final ResponseVo<T> response) {
        Objects.requireNonNull(ex, "ex must not be null");
        Objects.requireNonNull(response, "response must not be null");

        log.warn("", ex);

        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body(response);
    }

    @Nonnull
    private ResponseEntity<ResponseVo<String>> internalError(@Nonnull final Exception ex) {
        return error(Objects.requireNonNull(ex, "ex must not be null"), ResponseVo.internalError(ex.getMessage()));
    }

    @Nonnull
    private ResponseEntity<ResponseVo<String>> illegalParameters(@Nonnull final Exception ex) {
        return error(Objects.requireNonNull(ex, "ex must not be null"), ResponseVo.illegalParameters(ex.getMessage()));
    }
}
