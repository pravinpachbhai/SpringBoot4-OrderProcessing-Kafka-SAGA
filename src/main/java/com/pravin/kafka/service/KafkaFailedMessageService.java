package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.KafkaFailedMessageResponse;
import com.pravin.kafka.entity.FailedMessageStatus;
import com.pravin.kafka.entity.KafkaFailedMessage;
import com.pravin.kafka.repository.KafkaFailedMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KafkaFailedMessageService {

    private final KafkaFailedMessageRepository kafkaFailedMessageRepository;
    private final DataMapper dataMapper;

    public KafkaFailedMessageService(KafkaFailedMessageRepository kafkaFailedMessageRepository, DataMapper dataMapper) {
        this.kafkaFailedMessageRepository= kafkaFailedMessageRepository;
        this.dataMapper = dataMapper;
    }

    @Transactional
    public KafkaFailedMessage save(KafkaFailedMessage kafkaFailedMessage){
        return kafkaFailedMessageRepository.save(kafkaFailedMessage);
    }

    public List<KafkaFailedMessageResponse> getAll() {
        return kafkaFailedMessageRepository.findAll()
                .stream()
                .map(dataMapper::toResponse).toList();
    }

    public List<KafkaFailedMessageResponse> getByTopic(String topic) {
        return kafkaFailedMessageRepository.findByTopic(topic)
                .stream()
                .map(dataMapper::toResponse).toList();
    }

    public void markProcessed(Long id) {
        KafkaFailedMessage msg = kafkaFailedMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found: " + id));
        msg.setStatus(FailedMessageStatus.SUCCESS);
        msg.setLastRetryAt(LocalDateTime.now());
        kafkaFailedMessageRepository.save(msg);
    }

    public void incrementRetry(Long id) {
        KafkaFailedMessage msg = kafkaFailedMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found: " + id));

        msg.setRetryCount(msg.getRetryCount() + 1);
        msg.setStatus(FailedMessageStatus.RETRIED);
        msg.setLastRetryAt(LocalDateTime.now());
        kafkaFailedMessageRepository.save(msg);
    }

    public List<KafkaFailedMessageResponse> getFailedMessages() {
        return kafkaFailedMessageRepository.findByStatus(FailedMessageStatus.FAILED)
                .stream()
                .map(dataMapper::toResponse).toList();

    }


    public List<KafkaFailedMessageResponse> getFailedMessagesByTopic(String topic) {
        return kafkaFailedMessageRepository.findByStatusAndTopic(FailedMessageStatus.FAILED, topic)
                .stream()
                .map(dataMapper::toResponse).toList();
    }
}
