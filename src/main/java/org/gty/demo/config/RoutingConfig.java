package org.gty.demo.config;

import org.gty.demo.handler.UploadAndDownloadHandler;
import org.gty.demo.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RoutingConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(StudentHandler studentHandler,
                                                         UploadAndDownloadHandler uploadAndDownloadHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/student/{id}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
                        studentHandler::get)

                .andRoute(RequestPredicates.DELETE("/api/student/{id}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
                        studentHandler::delete)

                .andRoute(RequestPredicates.POST("/api/student")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
                                .and(RequestPredicates.contentType(MediaType.APPLICATION_JSON_UTF8)),
                        studentHandler::post)

                .andRoute(RequestPredicates.GET("/api/student")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
                        studentHandler::getByParameters)

                .andRoute(RequestPredicates.GET("/api/files/{filename:.+}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
                        uploadAndDownloadHandler::download);
    }
}
