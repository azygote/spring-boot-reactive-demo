package org.gty.demo.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

@RegisterMapper
interface MyBatisBaseMapper<T>
        extends Mapper<T>,
        InsertListMapper<T>,
        InsertUseGeneratedKeysMapper<T> {
}
