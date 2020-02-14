package org.gty.demo.service;

import org.gty.demo.model.entity.Student;
import org.gty.demo.model.vo.StudentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class ReactiveStudentService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveStudentService.class);

    private final StudentService studentService;
    private final Scheduler scheduler;

    public ReactiveStudentService(@Nonnull final StudentService studentService,
                                  @Nonnull final Scheduler scheduler) {
        this.studentService = Objects.requireNonNull(studentService, "studentService must not be null");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must not be null");
    }

    @Nonnull
    public Mono<StudentVo> findById(long id) {
        return Mono.fromCallable(() -> studentService.findById(id))
            .flatMap(Mono::justOrEmpty)
            .switchIfEmpty(Mono.error(new IllegalArgumentException(String.format("Student with id: [%s] could not be found.", id))))
            .subscribeOn(scheduler);
    }

    @Nonnull
    public Mono<Void> save(@Nonnull Student student) {
        Objects.requireNonNull(student, "id must not be null");

        return Mono.fromRunnable(() -> studentService.save(student))
            .cast(Void.class)
            .subscribeOn(scheduler);
    }

    @Nonnull
    public Mono<Page<StudentVo>> findByCondition(@Nonnull Pageable pageable) {
        Objects.requireNonNull(pageable, "orderBy must not be null");

        return Mono.fromCallable(() -> studentService.findByPage(pageable))
            .subscribeOn(scheduler);
    }

    @Nonnull
    public Mono<Void> delete(long id) {
        return Mono.fromRunnable(() -> studentService.delete(id))
            .cast(Void.class)
            .onErrorMap(throwable -> {
                if (throwable instanceof NoSuchElementException) {
                    return new IllegalArgumentException(String.format("Student with id: [%s] could not be found.", id));
                } else {
                    return throwable;
                }
            })
            .subscribeOn(scheduler);
    }
}
