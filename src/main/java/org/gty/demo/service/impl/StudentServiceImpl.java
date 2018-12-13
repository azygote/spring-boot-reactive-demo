package org.gty.demo.service.impl;

import org.gty.demo.constant.DeleteMark;
import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.repository.StudentRepository;
import org.gty.demo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(@Nonnull StudentRepository studentRepository) {
        this.studentRepository = Objects.requireNonNull(studentRepository,
                "studentRepository must not be null");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Cacheable(cacheNames = "students", keyGenerator = "keyGenerator")
    @Nonnull
    @Override
    public Optional<StudentVo> findById(long id) {
        return studentRepository.findByIdAndDeleteMark(id, DeleteMark.NOT_DELETED)
                .map(StudentVo::build);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Cacheable(cacheNames = "students", keyGenerator = "keyGenerator")
    @Nonnull
    @Override
    public Collection<StudentVo> findByName(@Nonnull String name) {
        Objects.requireNonNull(name, "name must not be null");

        return studentRepository.findByNameAndDeleteMark(name, DeleteMark.NOT_DELETED)
                .stream()
                .map(StudentVo::build)
                .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, readOnly = true, rollbackFor = Throwable.class)
    @Cacheable(cacheNames = "students", keyGenerator = "keyGenerator")
    @Nonnull
    @Override
    public Page<StudentVo> findByPage(@Nonnull Pageable pageable) {
        Objects.requireNonNull(pageable, "pageable must not be null");

        return studentRepository.findByDeleteMark(DeleteMark.NOT_DELETED, pageable).map(StudentVo::build);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Throwable.class)
    @CacheEvict(cacheNames = "students", keyGenerator = "keyGenerator", allEntries = true)
    @Override
    public void save(@Nonnull Student student) {
        Objects.requireNonNull(student, "student must not be null");

        studentRepository.saveAndFlush(student);
    }
}
