package org.gty.demo.repository;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public interface StudentRepository
        extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Nonnull
    Optional<Student> findByIdAndDeleteMark(long id, @Nonnull DeleteMark deleteMark);

    @Nonnull
    Collection<Student> findByNameContainingAndDeleteMark(@Nonnull String name, @Nonnull DeleteMark deleteMark);

    @Nonnull
    Page<Student> findByDeleteMark(@Nonnull DeleteMark deleteMark, @Nonnull Pageable pageable);
}
