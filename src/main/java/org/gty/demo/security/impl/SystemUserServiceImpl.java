package org.gty.demo.security.impl;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.mapper.SystemUserMapper;
import org.gty.demo.model.po.SystemUser;
import org.gty.demo.security.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private SystemUserMapper systemUserMapper;

    @Autowired
    private void injectBeans(SystemUserMapper systemUserMapper) {
        Objects.requireNonNull(systemUserMapper, "systemUserMapper must not be null");

        this.systemUserMapper = systemUserMapper;
    }

    @Cacheable(cacheNames = "systemUsers", keyGenerator = "keyGenerator")
    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Override
    @Nullable
    public SystemUser findUserByUsername(@Nonnull String username) {
        Objects.requireNonNull(username, "username must not be null");

        var example = Example.builder(SystemUser.class).build();
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("deleteMark", DeleteMark.NOT_DELETED.ordinal());

        var resultList = systemUserMapper.selectByExample(example);

        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }
}
