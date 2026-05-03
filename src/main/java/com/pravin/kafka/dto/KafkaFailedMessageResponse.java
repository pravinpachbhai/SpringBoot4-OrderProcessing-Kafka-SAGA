package com.pravin.kafka.dto;

import java.time.LocalDateTime;

public record KafkaFailedMessageResponse(
        Long id,
        String topic,
        Integer partitionId,
        Long offsetValue,
        String message,
        String error,
        LocalDateTime createdAt
) {
}
