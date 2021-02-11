package org.gty.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class AbstractJwtAuthenticationManager implements ReactiveAuthenticationManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private Scheduler scheduler = Schedulers.boundedElastic();

    private final UserDetailsChecker preAuthenticationChecks = this::defaultPreAuthenticationChecks;

    private UserDetailsChecker postAuthenticationChecks = this::defaultPostAuthenticationChecks;

    private void defaultPreAuthenticationChecks(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            logger.debug("User account is locked");
            throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                "User account is locked"));
        }
        if (!user.isEnabled()) {
            logger.debug("User account is disabled");
            throw new DisabledException(
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            logger.debug("User account is expired");
            throw new AccountExpiredException(messages
                .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
        }
    }

    private void defaultPostAuthenticationChecks(UserDetails user) {
        if (!user.isCredentialsNonExpired()) {
            logger.debug("User account credentials have expired");
            throw new CredentialsExpiredException(this.messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
        }
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        final var username = authentication.getName();

        return retrieveUser(username)
            .doOnNext(this.preAuthenticationChecks::check)
            .publishOn(this.scheduler)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))))
            .doOnNext(this.postAuthenticationChecks::check)
            .map(this::createUsernamePasswordAuthenticationToken);
    }

    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
            userDetails.getAuthorities());
    }

    public void setScheduler(@Nonnull final Scheduler scheduler) {
        this.scheduler = Objects.requireNonNull(scheduler, "[scheduler] must be null");
    }

    public void setPostAuthenticationChecks(@Nonnull final UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks =
            Objects.requireNonNull(postAuthenticationChecks, "[postAuthenticationChecks] must not be null");
    }

    protected abstract Mono<UserDetails> retrieveUser(String username);
}
