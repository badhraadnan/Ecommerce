package com.ecom.CommonEntity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stateId;

    private String stateName;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

//    @OneToMany(mappedBy = "state")
//    private List<City> city;

//    @OneToMany(mappedBy = "state")
//    private List<Address> address;




}
