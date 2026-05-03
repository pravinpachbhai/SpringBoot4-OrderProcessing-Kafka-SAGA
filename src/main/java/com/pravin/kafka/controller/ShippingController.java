package com.pravin.kafka.controller;

import com.pravin.kafka.dto.ShipmentRequest;
import com.pravin.kafka.dto.ShipmentResponse;
import com.pravin.kafka.service.ShippingService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shipments")
@Validated
public class ShippingController {

    private final ShippingService service;

    public ShippingController(ShippingService service) {
        this.service = service;
    }

    @PostMapping
    public ShipmentResponse create(@Valid  @RequestBody ShipmentRequest shipment) {
        return service.create(shipment);
    }
}