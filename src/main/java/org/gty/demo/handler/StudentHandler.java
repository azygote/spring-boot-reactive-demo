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
import org.springframework.core.ParameterizedTypeReference;
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

        var result = Mono.just(request.pathVariable("id"))
                .map(Long::valueOf)
                .flatMap(studentService::findById)
                .map(StudentVo::build)
                .<ResponseVo<?>>map(ResponseVo::success);

        return renderServerResponse(result);
    }

    @Nonnull
    public Mono<ServerResponse> getByParameters(ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var pageNumMono = Mono.justOrEmpty(request.queryParam("pageNum"))
                .filter(Predicate.not(String::isBlank))
                .map(Integer::valueOf)
                .defaultIfEmpty(0);

        var pageSizeMono = Mono.justOrEmpty(request.queryParam("pageSize"))
                .filter(Predicate.not(String::isBlank))
                .map(Integer::valueOf)
                .defaultIfEmpty(0);

        var orderByMono = Mono.justOrEmpty(request.queryParam("orderBy"))
                .defaultIfEmpty("");

        demoService.demo();

        var result = Mono.zip(pageNumMono, pageSizeMono, orderByMono)
                .flatMap(tuple3 -> studentService.findByCondition(tuple3.getT1(), tuple3.getT2(), tuple3.getT3()))
                .<ResponseVo<?>>map(ResponseVo::success);

        return renderServerResponse(result);
    }

    @Nonnull
    public Mono<ServerResponse> post(ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var result = request.bodyToMono(StudentForm.class)
                .publishOn(SystemConstants.defaultReactorScheduler())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be null")))
                .map(studentForm -> {
                    ValidationUtils.validate(studentForm);
                    return StudentForm.build(studentForm);
                })
                .flatMap(studentService::save)
                .<ResponseVo<?>>thenReturn(ResponseVo.success());

        return renderServerResponse(result);
    }

    private static Mono<ServerResponse> renderServerResponse(@Nonnull Mono<ResponseVo<?>> mono) {
        var temp = Objects.requireNonNull(mono, "mono must not be null")
                .onErrorResume(ExceptionHandler::renderErrorResponse);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(temp, new ParameterizedTypeReference<ResponseVo<?>>() {
                })
                .subscribeOn(SystemConstants.defaultReactorScheduler());
    }
}
