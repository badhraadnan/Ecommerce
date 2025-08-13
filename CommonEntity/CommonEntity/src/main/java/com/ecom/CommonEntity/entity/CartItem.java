package com.ecom.CommonEntity.entity;


import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemsId;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    private Double price;

    private int quantity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void setValue(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void updateValue(){
        this.updatedAt = LocalDateTime.now();
    }

}

