package com.pravin.kafka.dto;

public record NotificationRequest(
        String type,
        String recipient,
        String message
) {}
