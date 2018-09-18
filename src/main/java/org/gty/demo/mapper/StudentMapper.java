package org.gty.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.gty.demo.model.po.Student;

@Mapper
public interface StudentMapper extends MyBatisBaseMapper<Student> {
}
