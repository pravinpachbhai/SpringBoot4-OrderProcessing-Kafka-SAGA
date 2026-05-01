package com.pravin.kafka.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long productId;
    private Integer availableQuantity;
    private Integer reservedQuantity;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

}