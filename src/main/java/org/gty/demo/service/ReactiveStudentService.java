package org.gty.demo.service;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class ReactiveStudentService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveStudentService.class);

    private final StudentService studentService;

    @Autowired
    public ReactiveStudentService(@Nonnull StudentService studentService) {
        this.studentService = Objects.requireNonNull(studentService, "studentService must not be null");
    }

    @Nonnull
    public Mono<Student> findById(long id) {
        return Mono.fromCallable(() -> studentService.findById(id))
                .flatMap(Mono::justOrEmpty)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(String.format("Student with id: [%s] could not be found.", id))))
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }

    @Nonnull
    public Mono<Void> save(@Nonnull Student student) {
        Objects.requireNonNull(student, "id must not be null");

        return Mono.fromRunnable(() -> studentService.save(student))
                .cast(Void.class)
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }

    @Nonnull
    public Mono<Page<StudentVo>> findByCondition(@Nonnull Pageable pageable) {
        Objects.requireNonNull(pageable, "orderBy must not be null");

        return Mono.fromCallable(() -> studentService.findByPage(pageable))
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }
}
