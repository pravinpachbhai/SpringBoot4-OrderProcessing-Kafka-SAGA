package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.ShipmentRequest;
import com.pravin.kafka.dto.ShipmentResponse;
import com.pravin.kafka.entity.Shipment;
import com.pravin.kafka.entity.ShipmentStatus;
import com.pravin.kafka.event.PaymentSuccessEvent;
import com.pravin.kafka.event.ShipmentCreatedEvent;
import com.pravin.kafka.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@DependsOn("broker")
public class ShippingService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final ShipmentRepository repo;
    private final DataMapper dataMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ShippingService(ShipmentRepository repo, DataMapper dataMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repo = repo;
        this.dataMapper = dataMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-success", groupId = "shipping-group")
    public void ship(PaymentSuccessEvent event) {
        log.info("payment-success event received in shipping service for shipment.");
        kafkaTemplate.send("shipment-created", new ShipmentCreatedEvent(event.orderId()));
        log.info("Event publish for shipment-created.");
    }

    public ShipmentResponse create(ShipmentRequest shipmentRequest) {
        Shipment shipment = dataMapper.toEntity(shipmentRequest);
        shipment.setStatus(ShipmentStatus.CREATED);
        return  dataMapper.toResponse(repo.save(shipment));
    }
}