package org.gty.demo.security;

import org.apache.commons.lang3.StringUtils;
import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.entity.SystemUser;
import org.gty.demo.model.entity.SystemUserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DbUserDetailsService implements ReactiveUserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(DbUserDetailsService.class);

    private SystemUserService systemUserService;

    @Autowired
    public DbUserDetailsService(@Nonnull SystemUserService systemUserService) {
        this.systemUserService = Objects.requireNonNull(systemUserService, "systemUserService must not be null");
    }

    @Override
    @Nonnull
    public Mono<UserDetails> findByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        var userMono = findUserByUsername(username);
        var rolesMono = findRolesByUsername(username)
                .map(systemUserRole -> systemUserRole.getId().getRole())
                .map(DbUserDetailsService::convertAuthorities)
                .collect(Collectors.toUnmodifiableSet())
                .defaultIfEmpty(Set.of());

        return Mono.zip(userMono, rolesMono)
                .map(tuple2 -> {
                    var user = tuple2.getT1();
                    var roles = tuple2.getT2();

                    return new DbUserDetails(user.getUsername(), user.getPassword(), roles);
                });
    }

    @Nonnull
    private static GrantedAuthority convertAuthorities(@Nonnull String role) {
        Objects.requireNonNull(role, "role must not be null");

        return new SimpleGrantedAuthority("ROLE_" + StringUtils.upperCase(role));
    }

    @Nonnull
    private Mono<SystemUser> findUserByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        return Mono.fromCallable(() -> systemUserService.findUserByUsername(username))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .flatMap(Mono::justOrEmpty);
    }

    @Nonnull
    private Flux<SystemUserRole> findRolesByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        return Mono.fromCallable(() -> systemUserService.findRolesByUsername(username))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .flatMap(Mono::justOrEmpty)
                .flatMapMany(Flux::fromIterable);
    }
}
