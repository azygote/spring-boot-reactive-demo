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
    public Mono<Student> findById(@Nonnull Mono<Long> id) {
        return Objects.requireNonNull(id, "id must not be null")
                .flatMap(value ->
                        Mono.fromCallable(() -> studentService.findById(value))
                                .flatMap(Mono::justOrEmpty)
                                .subscribeOn(SystemConstants.defaultReactorScheduler()));
    }

    @Nonnull
    public Mono<Void> save(@Nonnull Mono<Student> student) {
        return Objects.requireNonNull(student, "id must not be null")
                .flatMap(value ->
                        Mono.<Void>fromRunnable(() -> studentService.save(value))
                                .subscribeOn(SystemConstants.defaultReactorScheduler()));
    }

    @Nonnull
    public Mono<PageInfo<StudentVo>> findByCondition(@Nonnull Mono<Integer> pageNumMono,
                                                     @Nonnull Mono<Integer> pageSizeMono,
                                                     @Nonnull Mono<String> orderByMono) {
        return Mono
                .zip(Objects.requireNonNull(pageNumMono, "pageNumMono must not be null"),
                        Objects.requireNonNull(pageSizeMono, "pageSizeMono must not be null"),
                        Objects.requireNonNull(orderByMono, "orderByMono must not be null"))
                .flatMap(tuple3 -> {
                    var pageNum = tuple3.getT1();
                    var pageSize = tuple3.getT2();
                    var orderBy = tuple3.getT3();

                    return Mono
                            .fromCallable(() -> studentService.findByCondition(pageNum, pageSize, orderBy))
                            .subscribeOn(SystemConstants.defaultReactorScheduler());
                })
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }
}
