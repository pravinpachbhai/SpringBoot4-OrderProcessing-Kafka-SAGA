package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.NotificationRequest;
import com.pravin.kafka.dto.NotificationResponse;
import com.pravin.kafka.entity.Notification;
import com.pravin.kafka.event.ShipmentCreatedEvent;
import com.pravin.kafka.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository repo;
    private final DataMapper dataMapper;

    public NotificationService(NotificationRepository repo, DataMapper dataMapper) {
        this.repo = repo;
        this.dataMapper = dataMapper;
    }

    @KafkaListener(topics = "shipment-created", groupId = "notification-group")
    public void notify(ShipmentCreatedEvent event,
                       @Header(org.springframework.kafka.support.KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("shipment-created event received in notification service to inform customer for Order Id." + key);
        log.info("Email sent for order: " + event.orderId());
    }

    public NotificationResponse send(NotificationRequest notificationRequest) {
        Notification notification = dataMapper.toEntity(notificationRequest);
        notification.setStatus("SENT");
        return dataMapper.toResponse(repo.save(notification));
    }
}