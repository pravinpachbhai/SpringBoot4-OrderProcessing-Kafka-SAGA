package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.KafkaFailedMessageResponse;
import com.pravin.kafka.entity.KafkaFailedMessage;
import com.pravin.kafka.repository.KafkaFailedMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
