package org.gty.demo.exception;

import java.io.Serial;

public class JwtAuthorizationException extends Exception {

    @Serial
    private static final long serialVersionUID = -6981208574315957751L;

    public JwtAuthorizationException() {
        super();
    }

    public JwtAuthorizationException(final Throwable throwable) {
        super(throwable);
    }
}
