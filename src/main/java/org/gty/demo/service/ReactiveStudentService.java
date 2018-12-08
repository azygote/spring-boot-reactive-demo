package org.gty.demo.service;

import com.github.pagehelper.PageInfo;
import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.po.Student;
import org.gty.demo.model.vo.StudentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;

@Service
public class ReactiveStudentService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveStudentService.class);

    private StudentService studentService;

    @Autowired
    private void injectBeans(StudentService studentService) {
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
    public Mono<PageInfo<StudentVo>> findByCondition(int pageNum,
                                                     int pageSize,
                                                     @Nonnull String orderBy) {
        Objects.requireNonNull(orderBy, "orderBy must not be null");

        return Mono.fromCallable(() -> studentService.findByCondition(pageNum, pageSize, orderBy))
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }
}
