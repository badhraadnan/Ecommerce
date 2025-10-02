package com.ecom.UserService.advice;


import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserServiceControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseModel handleNotFoundException(UserNotFoundException userNotFoundException){
        return new ResponseModel(HttpStatus.NOT_FOUND,null, userNotFoundException.getMessage());
    }

    @ExceptionHandler(AddressNotFound.class)
    public ResponseModel handleAddressNotFoundException(AddressNotFound addressNotFound){
        return new ResponseModel(HttpStatus.NOT_FOUND,null, addressNotFound.getMessage());
    }

    @ExceptionHandler(CountryNotFound.class)
    public ResponseModel handleCountryNotFoundException(CountryNotFound countryNotFound){
        return new ResponseModel(HttpStatus.NOT_FOUND,null, countryNotFound.getMessage());
    }

    @ExceptionHandler(StateNotFound.class)
    public ResponseModel handleStateNotFoundException(StateNotFound stateNotFound){
        return new ResponseModel(HttpStatus.NOT_FOUND,null, stateNotFound.getMessage());
    }

    @ExceptionHandler(CityNotFound.class)
    public ResponseModel handleCityNotFoundException(CityNotFound cityNotFound){
        return new ResponseModel(HttpStatus.NOT_FOUND,null, cityNotFound.getMessage());
    }
}
