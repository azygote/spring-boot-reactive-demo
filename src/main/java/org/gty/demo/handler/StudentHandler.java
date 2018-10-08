package org.gty.demo.handler;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.model.form.StudentForm;
import org.gty.demo.model.vo.ResponseVo;
import org.gty.demo.model.vo.StudentVo;
import org.gty.demo.service.ReactiveDemoService;
import org.gty.demo.service.ReactiveStudentService;
import org.gty.demo.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class StudentHandler {

    private static final Logger log = LoggerFactory.getLogger(StudentHandler.class);

    private ReactiveStudentService studentService;
    private ReactiveDemoService demoService;

    @Autowired
    private void injectBeans(ReactiveStudentService studentService,
                             ReactiveDemoService demoService) {
        this.studentService = Objects.requireNonNull(studentService, "studentService must not be null");
        this.demoService = Objects.requireNonNull(demoService, "demoService must not be null");
    }

    @Nonnull
    public Mono<ServerResponse> get(@Nonnull ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var idMono = Mono
                .just(request.pathVariable("id"))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .map(Long::valueOf);

        var resultMono = studentService
                .findById(idMono)
                .switchIfEmpty(idMono.flatMap(id -> Mono.error(new IllegalArgumentException(String.format("Student with id: [%s] could not be found.", id)))))
                .map(StudentVo::build)
                .map(ResponseVo::success)
                .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(result));

        return ExceptionHandler.renderErrorResponse(resultMono);
    }

    @Nonnull
    public Mono<ServerResponse> getByParameters(ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var pageNumMono = Mono
                .justOrEmpty(request.queryParam("pageNum"))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .filter(Predicate.not(String::isBlank))
                .map(Integer::valueOf)
                .defaultIfEmpty(0);

        var pageSizeMono = Mono
                .justOrEmpty(request.queryParam("pageSize"))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .filter(Predicate.not(String::isBlank))
                .map(Integer::valueOf)
                .defaultIfEmpty(0);

        var orderByMono = Mono
                .justOrEmpty(request.queryParam("orderBy"))
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .defaultIfEmpty("");

        demoService.demo();

        var resultMono = studentService.findByCondition(pageNumMono, pageSizeMono, orderByMono)
                .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(result));

        return ExceptionHandler.renderErrorResponse(resultMono);
    }

    @Nonnull
    public Mono<ServerResponse> post(ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var studentMono = request.bodyToMono(StudentForm.class)
                .publishOn(SystemConstants.defaultReactorScheduler())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be null")))
                .map(studentForm -> {
                    ValidationUtils.validate(studentForm);
                    return StudentForm.build(studentForm);
                });

        var resultMono = studentService
                .save(studentMono)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(ResponseVo.success()));

        return ExceptionHandler.renderErrorResponse(resultMono);
    }
}
