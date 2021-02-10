package org.gty.demo.security;

import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class JwtAuthenticationManager extends AbstractUserDetailsReactiveAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationManager(@Nonnull final DbUserDetailsService dbUserDetailsService,
                                    @Nonnull final PasswordEncoder passwordEncoder) {
        this.userDetailsService = Objects.requireNonNull(
            dbUserDetailsService,
            "[dbUserDetailsService] must not be null"
        );
        setPasswordEncoder(Objects.requireNonNull(passwordEncoder, "[passwordEncoder] must not be null"));
    }

    @Override
    protected Mono<UserDetails> retrieveUser(final String username) {
        return userDetailsService.findByUsername(username);
    }
}
