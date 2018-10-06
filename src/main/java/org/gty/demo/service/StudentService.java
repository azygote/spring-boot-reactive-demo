package org.gty.demo.service;

import com.github.pagehelper.PageInfo;
import org.gty.demo.model.po.Student;
import org.gty.demo.model.vo.StudentVo;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface StudentService {

    @Nonnull
    Optional<Student> findById(long id);

    @Nonnull
    Optional<Student> findByName(@Nonnull String name);

    @Nonnull
    PageInfo<StudentVo> findByCondition(int pageNum, int pageSize, @Nonnull String orderBy);

    void save(@Nonnull Student student);
}
