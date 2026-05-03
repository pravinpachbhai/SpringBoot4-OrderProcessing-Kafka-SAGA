package com.pravin.kafka.event;

public record InventoryFailedEvent(Long orderId, String reason) {
}