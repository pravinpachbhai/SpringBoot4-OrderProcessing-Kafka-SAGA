package com.pravin.kafka.event;

public record PaymentFailedEvent(Long orderId, String reason) {}
