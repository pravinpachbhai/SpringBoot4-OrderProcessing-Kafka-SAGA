package com.pravin.kafka.service;

import com.pravin.kafka.dto.KafkaFailedMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReprocessDLQService {
    private static final Logger log = LoggerFactory.getLogger(ReprocessDLQService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaFailedMessageService kafkaFailedMessageService;
    private static final int MAX_RETRIES = 3;

    public ReprocessDLQService(KafkaTemplate<String, Object> kafkaTemplate, KafkaFailedMessageService kafkaFailedMessageService){
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaFailedMessageService = kafkaFailedMessageService;
    }

    @Scheduled(fixedDelay = 60000) // every 1 min
    public synchronized void retryDLQJob() {
        retryDLQ();
    }

    public void retryDLQ(){
        List<KafkaFailedMessageResponse> messages = kafkaFailedMessageService.getFailedMessages();
        for (KafkaFailedMessageResponse msg : messages) {
            retry(msg);
        }
    }

    public void retryDLQ(String topic){
        List<KafkaFailedMessageResponse> messages = kafkaFailedMessageService.getFailedMessagesByTopic(topic);
        for (KafkaFailedMessageResponse msg : messages) {
            retry(msg);
        }
    }

    private void retry(KafkaFailedMessageResponse msg) {
        String originalTopic = getOriginalTopic(msg.topic());
        if (msg.retryCount() >= MAX_RETRIES) {
            log.warn("Max retries reached for id={}", msg.id());
            return;
        }

        if (msg.lastRetryAt() != null &&
                msg.lastRetryAt().plusMinutes(2).isAfter(LocalDateTime.now())) {
            return;
        }

        kafkaTemplate.send(originalTopic, msg.messageKey(), msg.message())
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Retry success for message id={}", msg.id());
                        kafkaFailedMessageService.markProcessed(msg.id());
                    } else {
                        log.error("Retry failed for message id={}", msg.id(), ex);
                        kafkaFailedMessageService.incrementRetry(msg.id());
                    }
                });
    }

    private String getOriginalTopic(String dltTopic) {
        if (!dltTopic.endsWith("-dlt")) {
            throw new IllegalArgumentException("Invalid DLT topic: " + dltTopic);
        }
        return dltTopic.substring(0, dltTopic.length() - 4);
    }
}
