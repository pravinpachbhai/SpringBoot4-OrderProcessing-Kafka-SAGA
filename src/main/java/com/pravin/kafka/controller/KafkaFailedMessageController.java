package com.pravin.kafka.controller;

import com.pravin.kafka.dto.KafkaFailedMessageResponse;
import com.pravin.kafka.service.KafkaFailedMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dlq")
@Validated
public class KafkaFailedMessageController {
    private final KafkaFailedMessageService kafkaFailedMessageService;

    public KafkaFailedMessageController(KafkaFailedMessageService kafkaFailedMessageService) {
        this.kafkaFailedMessageService = kafkaFailedMessageService;
    }

    @GetMapping("/{topic}")
    public ResponseEntity<List<KafkaFailedMessageResponse>> get(@PathVariable String topic) {
        return ResponseEntity.ok(kafkaFailedMessageService.getByTopic(topic));
    }

    @GetMapping
    public ResponseEntity<List<KafkaFailedMessageResponse>> getAll() {
        return ResponseEntity.ok(kafkaFailedMessageService.getAll());
    }
}
