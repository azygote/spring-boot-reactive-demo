package org.gty.demo.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "demo-queue")
public class DemoAmqpListener {

    private static final Logger log = LoggerFactory.getLogger(DemoAmqpListener.class);

    @RabbitHandler
    public void onReceive(@Payload String message) {
        log.debug("[AMQP] --- message received: {}", message);
    }
}
