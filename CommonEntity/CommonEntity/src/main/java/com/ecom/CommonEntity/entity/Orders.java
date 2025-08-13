package com.ecom.CommonEntity.entity;

import com.ecom.CommonEntity.Enum.OrderStatus;
import com.ecom.CommonEntity.dto.OrderDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne
    @JoinColumn(name = "addressId")
    private Address address;


    private double totalAmount;

    @OneToMany(mappedBy = "orders")
    @JsonIgnore
    private List<OrderItem> orderItem;

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