package org.gty.demo.security;

import org.gty.demo.model.po.SystemUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SystemUserService {

    @Nullable
    SystemUser findUserByUsername(@Nonnull String username) throws UsernameNotFoundException;
}
