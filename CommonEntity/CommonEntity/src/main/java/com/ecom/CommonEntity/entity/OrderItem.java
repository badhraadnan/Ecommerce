package com.ecom.CommonEntity.entity;

import com.ecom.CommonEntity.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "productID")
    private  Product product;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders orders;

    private int quantity;
    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void setValue(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


}
