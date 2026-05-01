package com.pravin.kafka.service;
import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.NotificationRequest;
import com.pravin.kafka.dto.NotificationResponse;
import com.pravin.kafka.entity.Notification;
import com.pravin.kafka.entity.User;
import com.pravin.kafka.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final DataMapper dataMapper;

    public NotificationService(NotificationRepository repo, DataMapper dataMapper) {
        this.repo = repo;
        this.dataMapper = dataMapper;
    }

    public NotificationResponse send(NotificationRequest notificationRequest) {
        Notification notification = dataMapper.toEntity(notificationRequest);
        notification.setStatus("SENT");
        return dataMapper.toResponse(repo.save(notification));
    }
}