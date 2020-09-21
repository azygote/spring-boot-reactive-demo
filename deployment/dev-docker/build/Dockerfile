FROM adoptopenjdk AS Builder
LABEL maintainer="Tianyu Ge <wierzard@hotmail.com>"
WORKDIR application
ARG LAYERED_JAR_FILE="target/*.jar"
COPY $LAYERED_JAR_FILE spring-boot-reactive-demo.jar
RUN java -Djarmode=layertools -jar spring-boot-reactive-demo.jar extract

FROM adoptopenjdk
LABEL maintainer="Tianyu Ge <wierzard@hotmail.com>"
WORKDIR application
COPY --from=Builder application/dependencies/ ./
COPY --from=Builder application/spring-boot-loader/ ./
COPY --from=Builder application/snapshot-dependencies/ ./
COPY --from=Builder application/application/ ./
EXPOSE 8080
ENV LOG_TEMP="/var/log/app-logs"
VOLUME $LOG_TEMP
ENV TINI_VERSION="v0.19.0"
ADD https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini /tini
RUN chmod +x /tini
ENTRYPOINT ["/tini", "--"]
CMD ["java", "org.springframework.boot.loader.JarLauncher"]
