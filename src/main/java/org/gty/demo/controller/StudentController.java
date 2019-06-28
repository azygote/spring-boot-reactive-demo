package org.gty.demo.controller;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.form.StudentForm;
import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.service.ReactiveDemoService;
import org.gty.demo.service.ReactiveStudentService;
import org.gty.demo.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.stream.StreamSupport;

@RestController
public final class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final ReactiveStudentService studentService;
    private final ReactiveDemoService demoService;

    public StudentController(@Nonnull final ReactiveStudentService studentService,
                             @Nonnull final ReactiveDemoService demoService) {
        this.studentService = Objects.requireNonNull(studentService, "studentService must not be null");
        this.demoService = Objects.requireNonNull(demoService, "demoService must not be null");
    }

    @Nonnull
    @GetMapping(value = "/api/student/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseVo<StudentVo>> get(@PathVariable("id") final long id) {
        return Mono
            .just(id)
            .flatMap(studentService::findById)
            .map(ResponseVo::success)
            .subscribeOn(SystemConstants.defaultReactorScheduler());
    }

    @Nonnull
    @GetMapping(value = "/api/student", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseVo<Page<StudentVo>>> getByParameters(@RequestParam(value = "page", required = false, defaultValue = "0") final int page,
                                                             @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
                                                             @Nonnull @RequestParam(value = "sort", required = false, defaultValue = "") final String sort) {

        final var responseVoMono = Mono
            .just(constructPageRequest(page, size, Objects.requireNonNull(sort, "sort must not be null")))
            .flatMap(studentService::findByCondition)
            .map(ResponseVo::success)
            .subscribeOn(SystemConstants.defaultReactorScheduler());

        return demoService.demo().then(responseVoMono);
    }

    @Nonnull
    @PostMapping(value = "/api/student",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseVo<Void>> post(@Nonnull @RequestBody final Mono<StudentForm> studentFormMono) {
        return Objects
            .requireNonNull(studentFormMono, "studentFormMono must not be null")
            .publishOn(SystemConstants.defaultReactorScheduler())
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be null")))
            .doOnSuccess(ValidationUtils::validate)
            .map(StudentForm::build)
            .flatMap(studentService::save)
            .<ResponseVo<Void>>thenReturn(ResponseVo.success())
            .subscribeOn(SystemConstants.defaultReactorScheduler());
    }

    @Nonnull
    @DeleteMapping(value = "/api/student/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Mono<ResponseVo<Void>> delete(@PathVariable("id") final long id) {
        return Mono
            .just(id)
            .flatMap(studentService::delete)
            .<ResponseVo<Void>>thenReturn(ResponseVo.success())
            .subscribeOn(SystemConstants.defaultReactorScheduler());
    }

    @Nonnull
    private static PageRequest constructPageRequest(final int page,
                                                    final int size,
                                                    @Nonnull final String sort) {
        final var tempIterable = Splitter.onPattern(",")
            .trimResults()
            .omitEmptyStrings()
            .split(Objects.requireNonNull(sort, "sort must not be null"));

        final var tempArray = StreamSupport.stream(tempIterable.spliterator(), true)
            .toArray(String[]::new);

        if (tempArray.length != 2) {
            throw new IllegalArgumentException("Unable to resolve SQL sort parameters: " + sort);
        }

        final var property = tempArray[0];
        final var order = tempArray[1];

        Sort.Order orderObject = null;
        if (StringUtils.equals(order, "asc")) {
            orderObject = Sort.Order.asc(property);
        } else if (StringUtils.equals(order, "desc")) {
            orderObject = Sort.Order.desc(property);
        }

        if (orderObject == null) {
            throw new IllegalArgumentException("Unable to resolve SQL sort parameters: " + sort);
        }

        return PageRequest.of(page, size, Sort.by(orderObject));
    }
}
