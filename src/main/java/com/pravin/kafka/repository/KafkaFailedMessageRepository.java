package com.pravin.kafka.repository;

import com.pravin.kafka.entity.FailedMessageStatus;
import com.pravin.kafka.entity.KafkaFailedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface KafkaFailedMessageRepository extends JpaRepository<KafkaFailedMessage, Long> {
    List<KafkaFailedMessage> findByTopic(String topic);

    List<KafkaFailedMessage> findByStatus(FailedMessageStatus status);

    List<KafkaFailedMessage> findByStatusAndTopic(FailedMessageStatus failedMessageStatus, String topic);
}
