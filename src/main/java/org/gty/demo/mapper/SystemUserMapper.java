package org.gty.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gty.demo.model.po.SystemUser;

@Mapper
public interface SystemUserMapper extends MyBatisBaseMapper<SystemUser> {
}
