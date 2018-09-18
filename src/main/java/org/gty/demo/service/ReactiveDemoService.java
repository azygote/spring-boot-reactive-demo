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
    private static final String message = "Hello, World";

    private AsyncAmqpTemplate asyncAmqpTemplate;
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @Autowired
    @SuppressWarnings("unchecked")
    private void injectBeans(AsyncAmqpTemplate asyncAmqpTemplate,
                             KafkaTemplate kafkaTemplate) {
        this.asyncAmqpTemplate = Objects.requireNonNull(asyncAmqpTemplate, "asyncAmqpTemplate must not be null");
        this.kafkaTemplate = (KafkaTemplate<Object, Object>) Objects.requireNonNull(kafkaTemplate, "kafkaTemplate must not be null");
    }

    public Mono<Void> demo() {
        var mono1 = Mono.<Void>fromRunnable(this::sendMessageToRabbit).subscribeOn(SystemConstants.defaultReactorScheduler());
        var mono2 = Mono.<Void>fromRunnable(this::sendMessageToKafka).subscribeOn(SystemConstants.defaultReactorScheduler());

        return mono1.then(mono2);
    }

    private void sendMessageToRabbit() {
        asyncAmqpTemplate.convertSendAndReceive("demo-queue", message);

        log.debug("[AMQP] --- message sent to rabbit");
    }

    private void sendMessageToKafka() {
        kafkaTemplate.send("demo-topic", SerializationUtils.serialize(message))
                .addCallback(result -> log.debug("[Kafka] --- message sent to kafka"), failure -> log.warn("sending message to kafka failed"));
    }
}
