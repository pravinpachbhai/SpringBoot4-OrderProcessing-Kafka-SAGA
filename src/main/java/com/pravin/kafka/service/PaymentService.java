package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.PaymentRequest;
import com.pravin.kafka.dto.PaymentResponse;
import com.pravin.kafka.entity.Payment;
import com.pravin.kafka.entity.PaymentStatus;
import com.pravin.kafka.entity.User;
import com.pravin.kafka.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final DataMapper dataMapper;
    public PaymentService(PaymentRepository repo, DataMapper dataMapper) {
        this.repo = repo;
        this.dataMapper = dataMapper;
    }

    public PaymentResponse process(PaymentRequest paymentRequest) {
        Payment payment = dataMapper.toEntity(paymentRequest);
        payment.setStatus(PaymentStatus.SUCCESS);
        return dataMapper.toResponse(repo.save(payment));
    }
}