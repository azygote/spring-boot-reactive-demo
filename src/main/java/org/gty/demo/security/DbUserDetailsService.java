package org.gty.demo.security;

import com.google.common.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.gty.demo.constant.SystemConstants;
import org.gty.demo.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DbUserDetailsService implements ReactiveUserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(DbUserDetailsService.class);

    private SystemUserService systemUserService;

    @Autowired
    private void injectBeans(SystemUserService systemUserService) {
        this.systemUserService = Objects.requireNonNull(systemUserService, "systemUserService must not be null");;
    }

    @Override
    @Nonnull
    public Mono<UserDetails> findByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        return Mono.fromCallable(() -> systemUserService.findUserByUsername(username))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .filter(user -> user.getRoles() != null)
                .map(user -> {
                    var roleList = JsonUtils.fromJson(user.getRoles(), new TypeToken<List<String>>() {
                    });

                    return new DbUserDetails(
                            user.getUsername(),
                            user.getPassword(),
                            Objects.requireNonNull(roleList).stream()
                                    .map(role -> new SimpleGrantedAuthority("ROLE_" + StringUtils.upperCase(role)))
                                    .collect(Collectors.toList()));
                });
    }
}
