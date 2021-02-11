package org.gty.demo.controller;

import org.gty.demo.controller.util.PageRequestUtils;
import org.gty.demo.controller.util.ValidationUtils;
import org.gty.demo.model.form.StudentForm;
import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.service.ReactiveDemoService;
import org.gty.demo.service.ReactiveStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.util.Objects;

@RestController
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final ReactiveStudentService studentService;
    private final ReactiveDemoService demoService;
    private final Scheduler scheduler;

    public StudentController(@Nonnull final ReactiveStudentService studentService,
                             @Nonnull final ReactiveDemoService demoService,
                             @Nonnull final Scheduler scheduler) {
        this.studentService = Objects.requireNonNull(studentService, "studentService must not be null");
        this.demoService = Objects.requireNonNull(demoService, "demoService must not be null");
        this.scheduler = Objects.requireNonNull(scheduler, "scheduler must not be null");
    }

    @Nonnull
    @GetMapping(value = "/api/student/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseVo<StudentVo>> get(@PathVariable("id") final long id) {
        return Mono
            .just(id)
            .flatMap(studentService::findById)
            .map(ResponseVo::success)
            .subscribeOn(scheduler);
    }

    @Nonnull
    @GetMapping(value = "/api/student", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseVo<Page<StudentVo>>> getByParameters(@RequestParam(value = "page", required = false, defaultValue = "0") final int page,
                                                             @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
                                                             @Nonnull @RequestParam(value = "sort", required = false, defaultValue = "") final String sort,
                                                             @Nonnull Mono<Authentication> authenticationMono) {

        final var responseVoMono = Mono
            .just(PageRequestUtils.constructPageRequest(page, size, Objects.requireNonNull(sort, "sort must not be null")))
            .flatMap(studentService::findByCondition)
            .map(ResponseVo::success)
            .subscribeOn(scheduler);

        var logAuthenticationMono = authenticationMono
            .doOnSuccess(authentication -> log.debug("Current user = {}", authentication.getName()));

        var demoMono = demoService.demo();

        return logAuthenticationMono.then(demoMono).then(responseVoMono);
    }

    @Nonnull
    @PostMapping(value = "/api/student",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseVo<Void>> post(@Nonnull @RequestBody final Mono<StudentForm> studentFormMono) {
        return Objects
            .requireNonNull(studentFormMono, "studentFormMono must not be null")
            .publishOn(scheduler)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be null")))
            .doOnNext(ValidationUtils::validate)
            .map(StudentForm::build)
            .flatMap(studentService::save)
            .<ResponseVo<Void>>thenReturn(ResponseVo.success())
            .subscribeOn(scheduler);
    }

    @Nonnull
    @DeleteMapping(value = "/api/student/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseVo<Void>> delete(@PathVariable("id") final long id) {
        return Mono
            .just(id)
            .flatMap(studentService::delete)
            .<ResponseVo<Void>>thenReturn(ResponseVo.success())
            .subscribeOn(scheduler);
    }
}
