package com.pravin.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pravin.kafka.entity.KafkaFailedMessage;
import com.pravin.kafka.entity.Order;
import com.pravin.kafka.entity.OrderItem;
import com.pravin.kafka.entity.OrderStatus;
import com.pravin.kafka.event.InventoryFailedEvent;
import com.pravin.kafka.event.PaymentFailedEvent;
import com.pravin.kafka.exception.ResourceNotFoundException;
import com.pravin.kafka.repository.OrderRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class OrderFailHandlerService {
    private static final Logger log = LoggerFactory.getLogger(OrderFailHandlerService.class);
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final KafkaFailedMessageService kafkaFailedMessageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderFailHandlerService(OrderRepository orderRepository,
                                   InventoryService inventoryService,
                                   PaymentService paymentService,
                                   KafkaFailedMessageService kafkaFailedMessageService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.kafkaFailedMessageService = kafkaFailedMessageService;
    }

    @KafkaListener(topics = "inventory-failed", groupId = "order-group")
    public void handleInventoryFail(InventoryFailedEvent event) {
        log.info("inventory event received in OrderHandler service to cancel order.");
        log.info("inventory failed for orderId={}", event.orderId());

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Idempotency guard
        if (order.getStatus() == OrderStatus.CANCELLED) {
            log.info("Order already cancelled: {}", event.orderId());
            return;
        }

        OrderStatus previousStatus = order.getStatus();
        // Mark failure first
        order.setStatus(OrderStatus.INVENTORY_FAILED);
        orderRepository.save(order);

        //  refund if payment already done
        if (previousStatus == OrderStatus.PAYMENT_SUCCESS) {
            refundPayment(order);
        }

        cancelOrder(event.orderId());
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-group")
    public void handlePaymentFail(PaymentFailedEvent event) {
        log.info("payment-failed received in OrderHandler service to cancel order.");
        log.info("payment failed for orderId={}", event.orderId());
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED) return;

        order.setStatus(OrderStatus.PAYMENT_FAILED);
        orderRepository.save(order);

        cancelOrder(event.orderId());
    }

    protected void cancelOrder(Long orderId) {
        log.info("Cancelling order started.");
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            log.info("Order already cancelled: {}", orderId);
            return;
        }
        OrderStatus previousStatus = order.getStatus();

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        //only release inventory if it was reserved
        boolean inventoryReserved = order.getStatus() == OrderStatus.INVENTORY_RESERVED;
        if (previousStatus == OrderStatus.INVENTORY_RESERVED) {
            for (OrderItem orderItem : order.getItems()) {
                inventoryService.release(orderItem.getProductId(), orderItem.getQuantity());
            }
        }
        log.info("Cancelling order and release quantity is done.");
    }

    @KafkaListener(
            topics = {"order-created-dlt", "payment-success-dlt"},
            groupId = "dlq-group"
    )
    public void handleDLQ(
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                          @Header(KafkaHeaders.OFFSET) long offset,
                          ConsumerRecord<String, Object> record) {
        try {
            log.error("DLQ message received: topic={}, partition={}, offset={}, payload={}",
                    topic, partition, offset, record);

            KafkaFailedMessage failed = new KafkaFailedMessage();
            failed.setTopic(topic);
            failed.setPartitionId(partition);
            failed.setMessageKey(record.key());
            failed.setOffsetValue(offset);
            failed.setMessage(objectMapper.writeValueAsString(record.value()));
            String error = record.headers().lastHeader("kafka_dlt-exception-message") != null
                    ? new String(record.headers()
                    .lastHeader("kafka_dlt-exception-message").value())
                    : "UNKNOWN";
            failed.setError(error);
            kafkaFailedMessageService.save(failed);

        } catch (Exception e) {
            log.error("Failed to persist DLQ message", e);
        }
    }

    private void refundPayment(Order order) {
        log.info("Refunding payment for orderId={}", order.getId());
        try {
            // call Payment service
            paymentService.refundPayment(order.getId());
            order.setStatus(OrderStatus.PAYMENT_REFUND_INITIATED);
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("Refund failed for orderId={}", order.getId(), e);
        }
    }

}
