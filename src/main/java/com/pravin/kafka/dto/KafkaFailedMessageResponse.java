package com.pravin.kafka.dto;

import com.pravin.kafka.entity.FailedMessageStatus;

import java.time.LocalDateTime;

public record KafkaFailedMessageResponse(
        Long id,
        String topic,
        Integer partitionId,
        Long offsetValue,
        String message,
        String error,
        int retryCount,
        String messageKey,
        FailedMessageStatus status,
        LocalDateTime lastRetryAt,
        LocalDateTime createdAt
) {
}
