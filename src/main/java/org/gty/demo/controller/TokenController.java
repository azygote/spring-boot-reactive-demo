package org.gty.demo.controller;

import org.gty.demo.controller.util.ValidationUtils;
import org.gty.demo.model.form.TokenForm;
import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.model.vo.TokenVo;
import org.gty.demo.service.TokenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

@RestController
public class TokenController {

    private final Scheduler scheduler;
    private final TokenService tokenService;

    public TokenController(@Nonnull final Scheduler scheduler,
                           @Nonnull final TokenService tokenService) {
        this.scheduler = Objects.requireNonNull(scheduler, "[scheduler] must not be null");
        this.tokenService = Objects.requireNonNull(tokenService, "[tokenService] must not be null");
    }

    @Nonnull
    @PostMapping(
        value = "/api/token",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseVo<TokenVo>> getToken(@Nonnull @RequestBody final Mono<TokenForm> tokenFormMono) {
        return Objects.requireNonNull(tokenFormMono, "[tokenFormMono] must not be null")
            .publishOn(scheduler)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be null")))
            .doOnNext(ValidationUtils::validate)
            .flatMap(tokenService::issueToken)
            .map(ResponseVo::success)
            .subscribeOn(scheduler);
    }
}
