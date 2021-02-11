package org.gty.demo.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class JwtAuthenticationManager extends AbstractJwtAuthenticationManager {

    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationManager(
        @Nonnull final DbUserDetailsService dbUserDetailsService,
        @Nonnull final Scheduler scheduler
    ) {
        this.userDetailsService = Objects.requireNonNull(
            dbUserDetailsService,
            "[dbUserDetailsService] must not be null"
        );
        setScheduler(Objects.requireNonNull(scheduler, "[scheduler] must not be null"));
    }

    @Override
    protected Mono<UserDetails> retrieveUser(final String username) {
        return userDetailsService.findByUsername(username);
    }
}
