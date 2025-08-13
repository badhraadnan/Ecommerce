package com.ecom.CommonEntity.dto;

import lombok.Data;

@Data
public class UserProfileDto {

    private String firstName;

    private String email;

    private String mobile;

    private String shortAddress;

    private String countryName;

    private String stateName;

    private String cityName;

    private Long zipCode;

    public UserProfileDto(String firstName, String email, String mobile ,String shortAddress,
                          String countryName, String stateName, String cityName, Long zipCode) {
        this.firstName = firstName;
        this.email = email;
        this.mobile = mobile;
        this.shortAddress = shortAddress;
        this.countryName = countryName;
        this.stateName = stateName;
        this.cityName = cityName;
        this.zipCode = zipCode;
    }

}
