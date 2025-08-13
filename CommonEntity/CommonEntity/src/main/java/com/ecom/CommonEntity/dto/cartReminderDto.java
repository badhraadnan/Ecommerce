package com.ecom.CommonEntity.dto;

import lombok.Data;

@Data
public class cartReminderDto {

    private String firstName;
    private String email;
    private String imageURL;
    private String name;
    private Double price;
    private int quantity;


    public cartReminderDto(String firstName, String email, String imageURL, String name, Double price, int quantity) {
        this.firstName = firstName;
        this.email = email;
        this.imageURL = imageURL;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
