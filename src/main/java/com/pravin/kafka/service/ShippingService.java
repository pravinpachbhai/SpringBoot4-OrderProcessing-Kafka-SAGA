package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.ShipmentRequest;
import com.pravin.kafka.dto.ShipmentResponse;
import com.pravin.kafka.entity.Shipment;
import com.pravin.kafka.entity.ShipmentStatus;
import com.pravin.kafka.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {

    private final ShipmentRepository repo;
    private final DataMapper dataMapper;

    public ShippingService(ShipmentRepository repo, DataMapper dataMapper) {
        this.repo = repo;
        this.dataMapper = dataMapper;
    }

    public ShipmentResponse create(ShipmentRequest shipmentRequest) {
        Shipment shipment = dataMapper.toEntity(shipmentRequest);
        shipment.setStatus(ShipmentStatus.CREATED);
        return  dataMapper.toResponse(repo.save(shipment));
    }
}