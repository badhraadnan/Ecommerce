package com.ecom.CommonEntity.entity;

import com.ecom.CommonEntity.Enum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String mobile;

    private String gender;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Address> address;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Orders> orders;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void setValue(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
