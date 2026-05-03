package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.OrderRequest;
import com.pravin.kafka.dto.OrderResponse;
import com.pravin.kafka.entity.Order;
import com.pravin.kafka.entity.OrderStatus;
import com.pravin.kafka.entity.Product;
import com.pravin.kafka.event.OrderCreatedEvent;
import com.pravin.kafka.event.OrderItemEvent;
import com.pravin.kafka.exception.ResourceNotFoundException;
import com.pravin.kafka.repository.OrderRepository;
import com.pravin.kafka.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final DataMapper dataMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, DataMapper dataMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.dataMapper = dataMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderResponse create(OrderRequest orderRequest) {

        BigDecimal totalAmount = orderRequest.items().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.productId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    return product.getPrice()
                            .multiply(BigDecimal.valueOf(item.quantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = dataMapper.toEntity(orderRequest);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);
        log.info("Order created.");
        // publish event
        OrderCreatedEvent event = new OrderCreatedEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getItems().stream()
                        .map(i -> new OrderItemEvent(i.getProductId(), i.getQuantity()))
                        .toList()
        );
        kafkaTemplate.send("order-created", String.valueOf(saved.getId()),event);
        log.info("Event publish order-created.");
        return dataMapper.toResponse(order);
    }

    public OrderResponse get(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return dataMapper.toResponse(order);
    }

    public List<OrderResponse> getByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(dataMapper::toResponse)
                .toList();
    }
}