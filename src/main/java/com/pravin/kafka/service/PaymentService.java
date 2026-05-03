package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.PaymentRequest;
import com.pravin.kafka.dto.PaymentResponse;
import com.pravin.kafka.entity.Payment;
import com.pravin.kafka.entity.PaymentStatus;
import com.pravin.kafka.event.InventoryReservedEvent;
import com.pravin.kafka.event.PaymentFailedEvent;
import com.pravin.kafka.event.PaymentSuccessEvent;
import com.pravin.kafka.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository repo;
    private final DataMapper dataMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentService(PaymentRepository repo, DataMapper dataMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repo = repo;
        this.dataMapper = dataMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "inventory-reserved", groupId = "payment-group")
    public void process(InventoryReservedEvent event) {
        log.info("inventory-reserved event received in payment service to process payment.");
        try {
            // simulate payment success
            kafkaTemplate.send("payment-success", new PaymentSuccessEvent(event.orderId()));
            log.info("Event publish for payment-success.");

        } catch (Exception e) {
            //kafkaTemplate.send("payment-failed", new PaymentFailedEvent(event.orderId(), e.getMessage()));
            //log.error("Event publish for payment-failed.", e);
            throw new RuntimeException("Retry this message");
        }
    }

    public PaymentResponse process(PaymentRequest paymentRequest) {
        Payment payment = dataMapper.toEntity(paymentRequest);
        payment.setStatus(PaymentStatus.SUCCESS);
        return dataMapper.toResponse(repo.save(payment));
    }
}