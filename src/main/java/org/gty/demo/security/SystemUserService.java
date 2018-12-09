package org.gty.demo.security;

import org.gty.demo.model.po.SystemUser;
import org.gty.demo.model.po.SystemUserRole;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public interface SystemUserService {

    @Nonnull
    Optional<SystemUser> findUserByUsername(@Nonnull String username);

    @Nonnull
    Optional<List<SystemUserRole>> findRolesByUsername(@Nonnull String username);
}
