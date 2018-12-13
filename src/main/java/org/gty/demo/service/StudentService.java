package org.gty.demo.service;

import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface StudentService {

    @Nonnull
    Optional<Student> findById(long id);

    @Nonnull
    Iterable<Student> findByName(@Nonnull String name);

    @Nonnull
    Page<StudentVo> findByPage(@Nonnull Pageable pageable);

    void save(@Nonnull Student student);
}
