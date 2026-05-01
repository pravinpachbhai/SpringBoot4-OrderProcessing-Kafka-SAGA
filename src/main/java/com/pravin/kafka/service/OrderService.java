package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.OrderRequest;
import com.pravin.kafka.dto.OrderResponse;
import com.pravin.kafka.entity.Order;
import com.pravin.kafka.entity.OrderStatus;
import com.pravin.kafka.entity.User;
import com.pravin.kafka.exception.ResourceNotFoundException;
import com.pravin.kafka.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final DataMapper dataMapper;

    public OrderService(OrderRepository repo, DataMapper dataMapper) {
        this.repo = repo;
        this.dataMapper = dataMapper;
    }

    public OrderResponse create(OrderRequest orderRequest) {
        Order order = dataMapper.toEntity(orderRequest);
        order.setStatus(OrderStatus.CREATED);
        return dataMapper.toResponse(repo.save(order));
    }

    public OrderResponse get(Long id) {
        Order order = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return dataMapper.toResponse(order);
    }

    public List<OrderResponse> getByUser(Long userId) {
        return repo.findByUserId(userId)
                .stream()
                .map(dataMapper::toResponse)
                .toList();
    }
}