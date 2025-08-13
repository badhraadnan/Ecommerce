package com.ecom.CommonEntity.entity;

import com.ecom.CommonEntity.Enum.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressId;

    private String shortAddress;

    private Long zipCode;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "stateId")
    private State state;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;
}

