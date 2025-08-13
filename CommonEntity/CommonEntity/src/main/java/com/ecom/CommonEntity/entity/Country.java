package com.ecom.CommonEntity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long countryId;

    private String countryName;

//    @OneToMany(mappedBy = "country")
//    private List<Address> address;


//    @OneToMany(mappedBy = "country")
//    private List<State> state;
}

