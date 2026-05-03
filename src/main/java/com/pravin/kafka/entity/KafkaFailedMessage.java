package com.pravin.kafka.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "kafka_failed_messages")
public class KafkaFailedMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private Integer partitionId;
    private Long offsetValue;

    @Lob
    private String message;

    @Lob
    private String error;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}