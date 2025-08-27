package com.ecom.UserService.service;

import com.ecom.CommonEntity.dto.AddressDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface AddressService {
    ResponseModel saveAddress(AddressDto addressDto);
    ResponseModel getAllAddress();

    //GET ADDRESS BY ID  ----User Side
    ResponseModel getAddressByUserId(Long userId);

    ResponseModel updateAddress(AddressDto addressDto);

    ResponseModel UserDetailsWithAddress();
    ResponseModel ListOfAllCountry();
    ResponseModel getAllCountryById(int id);

    ResponseModel ListOfAllState();
    ResponseModel CountryWiseListOfState(int id);

    ResponseModel ListOfAllCity();
    ResponseModel StateWiseListOfCity(int id);


    ResponseModel UserDetails();

}
