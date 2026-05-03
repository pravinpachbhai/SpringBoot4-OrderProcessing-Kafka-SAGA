package com.pravin.kafka.service;

import com.pravin.kafka.dto.KafkaFailedMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReprocessDLQService {
    private static final Logger log = LoggerFactory.getLogger(ReprocessDLQService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaFailedMessageService kafkaFailedMessageService;

    public ReprocessDLQService(KafkaTemplate<String, Object> kafkaTemplate, KafkaFailedMessageService kafkaFailedMessageService){
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaFailedMessageService = kafkaFailedMessageService;
    }

    public void retryDLQ(){
        List<KafkaFailedMessageResponse> messages = kafkaFailedMessageService.getAll();
        for (KafkaFailedMessageResponse msg : messages) {
            retry(msg);
        }
    }

    public void retryDLQ(String topic){
        List<KafkaFailedMessageResponse> messages = kafkaFailedMessageService.getByTopic(topic);
        for (KafkaFailedMessageResponse msg : messages) {
            retry(msg);
        }
    }

    private void retry(KafkaFailedMessageResponse msg) {
        kafkaTemplate.send(msg.topic().replace("-dlt", ""), msg.message());
    }
}
