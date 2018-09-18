package org.gty.demo.mapper;

import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@RegisterMapper
interface MyBatisBaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
