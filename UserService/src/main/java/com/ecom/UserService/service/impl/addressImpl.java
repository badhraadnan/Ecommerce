package com.ecom.UserService.service.impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.AddressDto;
import com.ecom.CommonEntity.dto.UserProfileDto;
import com.ecom.CommonEntity.entity.*;
import com.ecom.CommonEntity.model.ErrorMsg;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.SuccessMsg;
import com.ecom.UserService.exception.CityNotFound;
import com.ecom.UserService.exception.CountryNotFound;
import com.ecom.UserService.exception.StateNotFound;
import com.ecom.UserService.exception.UserNotFoundException;
import com.ecom.UserService.service.AddressService;
import com.ecom.commonRepository.dao.AddressDAO;
import com.ecom.commonRepository.dao.MasterDao;
import com.ecom.commonRepository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class addressImpl implements AddressService {

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private MasterDao masterDAO;

    @Autowired
    private UserDao userDAO;

    //Insert Address  --User Side
    @Override
    public ResponseModel saveAddress(AddressDto addressDto) {
        Country country = masterDAO.getCountryRepo().findById(addressDto.getCountryId())
                .orElseThrow(() -> new CityNotFound(ErrorMsg.COUNTRY_NOT_FOUND));
        State state = masterDAO.getStateRepo().findById(addressDto.getStateId())
                .orElseThrow(() -> new StateNotFound(ErrorMsg.STATE_NOT_FOUND));
        City city = masterDAO.getCityRepo().findById(addressDto.getCityId())
                .orElseThrow(() -> new CityNotFound(ErrorMsg.CITY_NOT_FOUND));
        User user = masterDAO.getUserRepository().findById(addressDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ErrorMsg.USER_NOT_FOUND));


        // Validate: State belongs to Country
        if (state.getCountry().getCountryId() != country.getCountryId()) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, "State does not belong to the selected Country");
        }

        // Validate: City belongs to State
        if (city.getState().getStateId() != state.getStateId()) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, "City does not belong to the selected State");
        }

        // All checks passed, proceed to save
        Address toEntity = AddressDto.toEntity(addressDto, country, state, city, user);
        Address savedAddress = addressDAO.saveAddress(toEntity);

        return new ResponseModel(HttpStatus.OK, AddressDto.toDto(savedAddress), SuccessMsg.ADDRESS_ADDED);
    }


    //GET ALL ADDRESS  ----Admin Side
    @Override
    public ResponseModel getAllAddress() {
        List<Address> existAddress = addressDAO.findAllByStatus(Status.ACTIVE);
        if (existAddress.isEmpty()) {
            return  new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.ADDRESS_NOT_FOUND);
        }else {
            List<AddressDto> dto =existAddress.stream()
                    .map(AddressDto::toDto)
                    .toList();
            return new ResponseModel(HttpStatus.OK,dto,SuccessMsg.USER_FETCHED_SUCCESSFULLY);
        }
    }

    //GET ADDRESS BY ID  ----User Side
    @Override
    public ResponseModel getAddressByUserId(Long userId) {
        List<Address> existAdd=addressDAO.findUserByIdAndStatus(userId,Status.ACTIVE);
        if (existAdd != null){
            List<AddressDto> dto= existAdd.stream()
                    .map(AddressDto::toDto)
                    .toList();
            return new ResponseModel(HttpStatus.OK,dto,SuccessMsg.ADDRESS_FETCHED_SUCCESSFULLY);
        }
        return new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.ADDRESS_NOT_FOUND);
    }

    //UPDATE ADDRESS
//    @Override
//    public ResponseModel updateAddress(AddressDto addressDto) {
//        try {
//
//            Optional<Country> existCountry = masterDAO.getCountryRepo().findById(addressDto.getCountryId());
//            Optional<State> existState = masterDAO.getStateRepo().findById(addressDto.getStateId());
//            Optional<City> existCity = masterDAO.getCityRepo().findById(addressDto.getCityId());
//            Optional<User> existUser = masterDAO.getUserRepository().findById(addressDto.getUserId());
//
//            Optional<Address> existAddress = masterDAO.getAddressRepository().findByAddressIdAndStatus(addressDto.getAddressId(), Status.ACTIVE);
//            if (existAddress.isPresent()) {
//                Address address = existAddress.get();
//                AddressDto.toUpdate(addressDto, address, existCountry.get(), existState.get(), existCity.get(), existUser.get());
//                Address saveAddress = addressDAO.saveAddress(address);
//                return new ResponseModel(HttpStatus.OK, AddressDto.toDto(saveAddress), "Address Updated");
//            } else {
//                return new ResponseModel(HttpStatus.OK, null, "Address Not Updated");
//            }
//        } catch (Exception e) {
//            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR,null,"Address Not Updated Due to some ERROR");
//        }
//    }

    //UPDATE ADDRESS-2  ----User Side
    @Override
    public ResponseModel updateAddress(AddressDto addressDto) {
        try {
            Address address = addressDAO.findByUserIdAndStatus(addressDto.getAddressId(), Status.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("Address Not Exist"));

            // Load references only if IDs are provided
            Country country = null;
            if (addressDto.getCountryId() != null) {
                country = masterDAO.getCountryRepo().findById(addressDto.getCountryId()).orElseThrow(() -> new CountryNotFound("Country Not Found"));
            }

            State state = null;
            if (addressDto.getStateId() != null) {
                state = masterDAO.getStateRepo().findById(addressDto.getStateId()).orElseThrow(() -> new StateNotFound("State Not Found"));
            }

            City city = null;
            if (addressDto.getCityId() != null) {
                city = masterDAO.getCityRepo().findById(addressDto.getCityId()).orElseThrow(() -> new CityNotFound("City Not Found"));
            }

            User user = null;
            if (addressDto.getUserId() != null) {
                user = masterDAO.getUserRepository().findById(addressDto.getUserId()).orElseThrow(() -> new UserNotFoundException("User Not Found"));
            }

            // Update the address only with non-null values
            AddressDto.toUpdate(addressDto, address, country, state, city, user);

            Address savedAddress = addressDAO.saveAddress(address);
            return new ResponseModel(HttpStatus.OK, AddressDto.toDto(savedAddress), SuccessMsg.ADDRESS_UPDATED);

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Address Not Updated Due to an ERROR");
        }

    }




    //USER AND ADDRESS DETAILS  ----User Side
    @Override
    public ResponseModel UserDetailsWithAddress() {
        List<Object[]> user = addressDAO.findUsersAllDetailsByAddress();
        if (user.isEmpty()){
            return new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.USER_NOT_FOUND);
        }else {
            return new ResponseModel(HttpStatus.OK, user, SuccessMsg.USER_FETCHED_SUCCESSFULLY);
        }
    }


    //LIST OF COUNTRY  ----User Side
    @Override
    public ResponseModel ListOfAllCountry() {
        List<Country> countries = masterDAO.getCountryRepo().findAll();
        if(countries.isEmpty()){
            return new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.COUNTRY_NOT_FOUND);
        }else {
            return new ResponseModel(HttpStatus.OK, countries, "Success");
        }
    }

    //GEY COUNTRY BY ID  ----User Side
    @Override
    public ResponseModel getAllCountryById(int id) {
        System.out.println("method called..");
        List<Country> countries=masterDAO.getCountryRepo().findAllByCountryId(id);
        if(countries.isEmpty()){
            return new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.COUNTRY_NOT_FOUND);
        }else {
            return new ResponseModel(HttpStatus.OK, countries, "Success");
        }
    }

    //LIST OF STATE  ----User Side
    @Override
    public ResponseModel ListOfAllState() {
        List<State> states=masterDAO.getStateRepo().findAll();
        return new ResponseModel(HttpStatus.OK,states,"Success");
    }

    //COUNTRY WISE LIST OF STATE  ----User Side
    @Override
    public ResponseModel CountryWiseListOfState(int id) {
        Optional<Country> existCountry = masterDAO.getCountryRepo().findById((long) id);
        if (existCountry.isPresent()) {
            List<State> states = masterDAO.getStateRepo().listOfStateCountryWise(id);
            return new ResponseModel(HttpStatus.OK, states, "Success");
        }else {
            return new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.COUNTRY_NOT_FOUND);
        }
    }

    //LIST OF CITY ----User Side
    @Override
    public ResponseModel ListOfAllCity() {
        List<City> cites = masterDAO.getCityRepo().findAll();
        return new ResponseModel(HttpStatus.OK,cites,"Success");
    }

    //STATE WISE LIST OF CITY  ----User Side
    @Override
    public ResponseModel StateWiseListOfCity(int id) {
        Optional<State> existState = masterDAO.getStateRepo().findById((long) id);
        if (existState.isPresent()) {
            List<City> cities = masterDAO.getCityRepo().listOfCityStateWise(id);
            return new ResponseModel(HttpStatus.OK, cities, "Success");
        }else {
            return new ResponseModel(HttpStatus.NOT_FOUND,null,ErrorMsg.STATE_NOT_FOUND);
        }
    }

    //User Profile  --User Side
    @Override
    public ResponseModel UserDetails() {
        List<UserProfileDto> user = addressDAO.findUser();
        if (user.isEmpty()){
            return  new ResponseModel(HttpStatus.NOT_FOUND,null, ErrorMsg.USER_NOT_FOUND);
        }else {
            return new ResponseModel(HttpStatus.OK, user, SuccessMsg.USER_FETCHED_SUCCESSFULLY);
        }
    }


}
