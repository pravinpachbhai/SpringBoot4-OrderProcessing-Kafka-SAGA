package com.pravin.kafka.dto;

import java.util.List;

public record OrderRequest(
        Long userId,
        List<OrderItemRequest> items
) {}