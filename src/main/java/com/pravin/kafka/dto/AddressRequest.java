package com.pravin.kafka.dto;

public record AddressRequest(
        String addressLine,
        String city,
        String state,
        String zip
) {}