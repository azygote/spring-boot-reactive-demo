package org.gty.demo.handler;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

@Component
public class StudentHandler {

    private static final Logger log = LoggerFactory.getLogger(StudentHandler.class);

    private final ReactiveStudentService studentService;
    private final ReactiveDemoService demoService;

    @Autowired
    public StudentHandler(ReactiveStudentService studentService,
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

        var pageMono = Mono.justOrEmpty(request.queryParam("page"))
                .filter(Predicate.not(String::isBlank))
                .map(Integer::valueOf)
                .defaultIfEmpty(0);

        var sizeMono = Mono.justOrEmpty(request.queryParam("size"))
                .filter(Predicate.not(String::isBlank))
                .map(Integer::valueOf)
                .defaultIfEmpty(0);

        var sortMono = Mono.justOrEmpty(request.queryParam("sort"))
                .defaultIfEmpty("");

        demoService.demo();

        var result = Mono.zip(pageMono, sizeMono, sortMono)
                .map(tuple3 -> {
                    var page = tuple3.getT1();
                    var size = tuple3.getT2();
                    var sort = tuple3.getT3();

                    var tempIterable = Splitter.onPattern(",")
                            .trimResults()
                            .omitEmptyStrings()
                            .split(sort);

                    var tempArray = StreamSupport.stream(tempIterable.spliterator(), true)
                            .toArray(String[]::new);

                    if (tempArray.length == 2) {
                        var property = tempArray[0];
                        var order = tempArray[1];

                        Sort.Order orderObject = null;
                        if (StringUtils.equals(order, "asc")) {
                            orderObject = Sort.Order.asc(property);
                        } else if (StringUtils.equals(order, "desc")) {
                            orderObject = Sort.Order.desc(property);
                        }

                        if (orderObject != null) {
                            return PageRequest.of(page, size, Sort.by(orderObject));
                        }
                    }

                    throw new IllegalArgumentException("Unable to resolve SQL sort parameters: " + sort);
                })
                .flatMap(studentService::findByCondition)
                .<ResponseVo<?>>map(ResponseVo::success);

        return renderServerResponse(result);
    }

    @Nonnull
    public Mono<ServerResponse> post(ServerRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        var result = request.bodyToMono(StudentForm.class)
                .publishOn(SystemConstants.defaultReactorScheduler())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body cannot be null")))
                .doOnSuccess(ValidationUtils::validate)
                .map(StudentForm::build)
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
