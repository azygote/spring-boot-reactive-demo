package org.gty.demo.security.impl;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.SystemUser;
import org.gty.demo.model.entity.SystemUserRole;
import org.gty.demo.repository.SystemUserRepository;
import org.gty.demo.repository.SystemUserRoleRepository;
import org.gty.demo.security.SystemUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private final SystemUserRepository systemUserRepository;
    private final SystemUserRoleRepository systemUserRoleRepository;

    public SystemUserServiceImpl(@Nonnull SystemUserRepository systemUserRepository,
                                  @Nonnull SystemUserRoleRepository systemUserRoleRepository) {
        this.systemUserRepository = Objects.requireNonNull(systemUserRepository,
                "systemUserRepository must not be null");

        this.systemUserRoleRepository = Objects.requireNonNull(systemUserRoleRepository,
                "systemUserRoleRepository must not be null");
    }

    @Cacheable(cacheNames = "systemUsers", keyGenerator = "keyGenerator")
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Override
    @Nonnull
    public Optional<SystemUser> findUserByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        return systemUserRepository.findByUsernameAndDeleteMark(username, DeleteMark.NOT_DELETED);
    }

    @Cacheable(cacheNames = "systemUserRoles", keyGenerator = "keyGenerator")
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Nonnull
    @Override
    public Collection<SystemUserRole> findRolesByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        return systemUserRoleRepository.findByIdUsernameAndDeleteMark(username, DeleteMark.NOT_DELETED);
    }
}
