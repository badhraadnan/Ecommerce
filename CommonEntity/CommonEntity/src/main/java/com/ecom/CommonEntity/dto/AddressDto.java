package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    private Long addressId;

    private String shortAddress;

    private Long countryId;

    private Long stateId;

    private Long cityId;

    private Long zipCode;

    private Long userId;

    private Status status;

    public static Address toEntity(AddressDto addressDto, Country country, State state, City city, User user) {
        return Address.builder()
                .shortAddress(addressDto.getShortAddress())
                .country(country).state(state)
                .city(city)
                .zipCode(addressDto.getZipCode())
                .status(Status.ACTIVE)
                .user(user)
                .build();
    }

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                .addressId(address.getAddressId())
                .shortAddress(address.getShortAddress())
                .countryId(address.getCountry().getCountryId())
                .stateId(address.getState().getStateId())
                .cityId(address.getCity().getCityId())
                .zipCode(address.getZipCode())
                .userId(address.getUser().getUserId())
                .status(address.getStatus())
                .build();
    }

    public static void toUpdate(AddressDto dto, Address address, Country country, State state, City city, User user) {
       if(dto.getShortAddress() != null){
           address.setShortAddress(dto.getShortAddress());
       }
       if (country != null){
           address.setCountry(country);
       }
       if (state != null){
           address.setState(state);
       }
       if (city != null){
           address.setCity(city);
       }
       if (user != null) {
           address.setUser(user);
       }
       if(dto.getZipCode() !=null) {
           address.setZipCode(dto.getZipCode());
       }
    }
}