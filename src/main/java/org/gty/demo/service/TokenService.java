package org.gty.demo.service;

import org.gty.demo.model.form.TokenForm;
import org.gty.demo.model.vo.TokenVo;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public interface TokenService {

    @Nonnull
    Mono<TokenVo> issueToken(@Nonnull final TokenForm tokenForm);
}
