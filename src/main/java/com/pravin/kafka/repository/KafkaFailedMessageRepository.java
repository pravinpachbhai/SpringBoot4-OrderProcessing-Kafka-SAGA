package com.pravin.kafka.repository;

import com.pravin.kafka.entity.KafkaFailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KafkaFailedMessageRepository extends JpaRepository<KafkaFailedMessage, Long> {
    List<KafkaFailedMessage> findByTopic(String topic);
}
