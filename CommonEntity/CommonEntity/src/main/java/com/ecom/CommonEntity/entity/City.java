package com.ecom.CommonEntity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cityId;

    private String cityName;

    @ManyToOne
    @JoinColumn(name = "stateId")
    private State state;

//    @OneToMany(mappedBy = "city")
//    private List<Address> address;



}

