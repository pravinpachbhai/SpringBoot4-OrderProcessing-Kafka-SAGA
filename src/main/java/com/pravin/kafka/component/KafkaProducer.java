package com.pravin.kafka.component;


import com.pravin.kafka.entity.KafkaFailedMessage;
import com.pravin.kafka.service.KafkaFailedMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaFailedMessageService kafkaFailedMessageService;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaFailedMessageService kafkaFailedMessageService) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaFailedMessageService = kafkaFailedMessageService;
    }

    public void send(String topic, String key, String payload) {
        if (topic == null || topic.isBlank()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
        LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
        kafkaTemplate.send(topic, key, payload).whenComplete((result, ex) -> {
            if (ex == null) {
                LOGGER.info("Sent to partition={}, offset={}",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                LOGGER.error("Failed to send message to topic='{}'", topic, ex);
                saveFailureMessage(topic, payload, ex);
            }
        });
    }

    private void saveFailureMessage(String topic, String  payload, Throwable ex) {
        try {
            KafkaFailedMessage failed = new KafkaFailedMessage();
            failed.setTopic(topic);
            failed.setMessage(payload);
            failed.setError(ex.getMessage());
            kafkaFailedMessageService.save(failed);
            LOGGER.error("Saved failed Kafka message to DB for topic={}", topic, ex);
        } catch (Exception dbEx) {
            LOGGER.error("CRITICAL: Failed to persist Kafka failure to DB", dbEx);
        }
    }
}