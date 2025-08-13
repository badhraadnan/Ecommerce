package com.ecom.CommonEntity.entity;

import com.ecom.CommonEntity.Enum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "catId")
    private Category category;


    private Double price;
    private int qty;
    private String imageURL;
    private int taxRate;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem > cartItem;


    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void setValue() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}