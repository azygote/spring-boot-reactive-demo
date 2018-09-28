package org.gty.demo.service;

import org.apache.commons.lang3.SerializationUtils;
import org.gty.demo.constant.SystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class ReactiveDemoService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveDemoService.class);
    private static final String MESSAGE = "Hello, World";

    private AsyncAmqpTemplate asyncAmqpTemplate;
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    @SuppressWarnings("unchecked")
    private void injectBeans(AsyncAmqpTemplate asyncAmqpTemplate,
                             KafkaTemplate kafkaTemplate) {
        this.asyncAmqpTemplate = Objects.requireNonNull(asyncAmqpTemplate, "asyncAmqpTemplate must not be null");
        this.kafkaTemplate = (KafkaTemplate<Object, Object>) Objects.requireNonNull(kafkaTemplate, "kafkaTemplate must not be null");
    }

    public void demo() {
        Mono.zip(sendMessageToRabbit(), sendMessageToKafka())
                .subscribe();
    }

    private Mono<Integer> sendMessageToRabbit() {
        return Mono
                .<Integer>fromRunnable(() -> {
                    asyncAmqpTemplate.convertSendAndReceive("demo-queue", MESSAGE);
                    log.debug("[AMQP] --- MESSAGE sent to rabbit");
                })
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .defaultIfEmpty(0);
    }

    private Mono<Integer> sendMessageToKafka() {
        return Mono
                .<Integer>fromRunnable(() -> {
                    kafkaTemplate.send("demo-topic", SerializationUtils.serialize(MESSAGE))
                            .addCallback(result -> log.debug("[Kafka] --- MESSAGE sent to kafka"),
                                    failure -> log.warn("sending MESSAGE to kafka failed"));
                })
                .subscribeOn(SystemConstants.defaultReactorScheduler())
                .defaultIfEmpty(0);
    }
}
