package com.pravin.kafka.service;

import com.pravin.kafka.component.DataMapper;
import com.pravin.kafka.dto.InventoryResponse;
import com.pravin.kafka.entity.Inventory;
import com.pravin.kafka.event.InventoryReservedEvent;
import com.pravin.kafka.event.OrderCreatedEvent;
import com.pravin.kafka.exception.InsufficientStockException;
import com.pravin.kafka.exception.ResourceNotFoundException;
import com.pravin.kafka.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository repo;
    private final DataMapper dataMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryService(InventoryRepository repo, DataMapper dataMapper, KafkaTemplate<String, Object> kafkaTemplate, OrderService orderService) {
        this.repo = repo;
        this.dataMapper = dataMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    public void handle(OrderCreatedEvent event) {
        log.info("order-created event received in inventory service to reserve the qty.");
        try {
            event.items().forEach(item -> reserve(item.productId(), item.quantity()));

            kafkaTemplate.send("inventory-reserved", new InventoryReservedEvent(event.orderId()));
            log.info("Event publish for inventory-reserved.");

        } catch (Exception e) {
            //kafkaTemplate.send("inventory-failed", new InventoryFailedEvent(event.orderId(), e.getMessage()));
            //log.error("Event publish for inventory-failed.", e);
            throw new RuntimeException("Retry this message");
        }
    }

    public InventoryResponse get(Long productId) {
        Inventory inventory =  repo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        return dataMapper.toResponse(inventory);
    }

    public InventoryResponse reserve(Long productId, int qty) {
        Inventory inventory =  repo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));

        if (inventory.getAvailableQuantity() < qty) {
            throw new InsufficientStockException("Not enough stock for product: " + productId);
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - qty);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + qty);

        return dataMapper.toResponse(repo.save(inventory));
    }

    public void release(Long productId, int qty) {
        Inventory inventory = repo.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found"));
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + qty);
        inventory.setReservedQuantity(inventory.getReservedQuantity() - qty);
    }
}