package org.gty.demo.service.listener;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DemoKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(DemoKafkaListener.class);

    @KafkaListener(id = "${spring.kafka.consumer.group-id}",
        topics = "${spring.kafka.template.default-topic}")
    public void onReceive(ConsumerRecord<String, byte[]> record) {
        log.debug("[Kafka] --- message received: {}", SerializationUtils.<Object>deserialize(record.value()));
    }
}
