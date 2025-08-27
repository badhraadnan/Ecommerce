package com.ecom.UserService.controller;

import com.ecom.CommonEntity.dto.AddressDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/service/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/")
    public ResponseModel saveAddress(@RequestBody AddressDto addressDto){
        return addressService.saveAddress(addressDto);
    }

    @GetMapping("/")
    public ResponseModel getAllAddress(){
        return addressService.getAllAddress();
    }

    @GetMapping("/id/{userId}")
    public ResponseModel getAddressByID(@RequestParam Long userId){
        return addressService.getAddressByUserId(userId);
    }

    @PutMapping("/")
    public ResponseModel updateAddress(@RequestBody AddressDto addressDto){
        return addressService.updateAddress(addressDto);
    }

    @GetMapping("/join")
    public ResponseModel UserDetailsWithAddress(){
        return addressService.UserDetailsWithAddress();
    }

    @GetMapping("/country/{id}")
    public ResponseModel getAllCountryById(@PathVariable int id){
        return addressService.getAllCountryById(id);
    }

    @GetMapping("/state/{id}")
    public ResponseModel getAllStateById(@PathVariable  int id){
        return addressService.CountryWiseListOfState(id);
    }

    @GetMapping("/city/{id}")
    public ResponseModel getAllCityById(@PathVariable int id){
        return addressService.StateWiseListOfCity(id);
    }

    @GetMapping("/country")
    public ResponseModel ListOfCountry(){
        return addressService.ListOfAllCountry();
    }

    @GetMapping("/state")
    public ResponseModel ListOfState(){
        return addressService.ListOfAllState();
    }

    @GetMapping("/city")
    public ResponseModel ListOfCity(){
        return addressService.ListOfAllCity();
    }

    @GetMapping("/user")
    public ResponseModel UserLogin(){
        return addressService.UserDetails();
    }
}
