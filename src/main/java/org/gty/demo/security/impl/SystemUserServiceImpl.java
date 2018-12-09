package org.gty.demo.security.impl;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.mapper.SystemUserMapper;
import org.gty.demo.mapper.SystemUserRoleMapper;
import org.gty.demo.model.po.SystemUser;
import org.gty.demo.model.po.SystemUserRole;
import org.gty.demo.security.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private SystemUserMapper systemUserMapper;
    private SystemUserRoleMapper systemUserRoleMapper;

    @Autowired
    private void injectBeans(SystemUserMapper systemUserMapper,
                             SystemUserRoleMapper systemUserRoleMapper) {
        this.systemUserMapper = Objects.requireNonNull(systemUserMapper, "systemUserMapper must not be null");

        this.systemUserRoleMapper = Objects.requireNonNull(systemUserRoleMapper,
                "systemUserRoleMapper must not be null");
    }

    @Cacheable(cacheNames = "systemUsers", keyGenerator = "keyGenerator")
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Override
    @Nonnull
    public Optional<SystemUser> findUserByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        var example = Example.builder(SystemUser.class).build();
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("deleteMark", DeleteMark.NOT_DELETED.ordinal());

        var resultList = systemUserMapper.selectByExample(example);

        return Optional.ofNullable(resultList)
                .filter(Predicate.not(List::isEmpty))
                .flatMap(list -> Optional.ofNullable(list.get(0)));
    }

    @Cacheable(cacheNames = "systemUserRoles", keyGenerator = "keyGenerator")
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Nonnull
    @Override
    public Optional<List<SystemUserRole>> findRolesByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        var example = Example.builder(SystemUserRole.class).build();
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("deleteMark", DeleteMark.NOT_DELETED.ordinal());

        var resultList = systemUserRoleMapper.selectByExample(example);

        return Optional.ofNullable(resultList);
    }
}
