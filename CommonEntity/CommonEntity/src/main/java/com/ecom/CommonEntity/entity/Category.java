package com.ecom.CommonEntity.entity;


import com.ecom.CommonEntity.Enum.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.sqm.tree.domain.SqmTreatedBagJoin;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long catID;
    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "category",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> product;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void setValue(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}

