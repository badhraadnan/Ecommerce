package com.ecom.UserService.service.impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.AddressDto;
import com.ecom.CommonEntity.dto.UserProfileDto;
import com.ecom.CommonEntity.entity.*;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.service.AddressService;
import com.ecom.commonRepository.dao.AddressDAO;
import com.ecom.commonRepository.dao.MasterDao;
import com.ecom.commonRepository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Optional<Country> existCountry = masterDAO.getCountryRepo().findById(addressDto.getCountryId());
        Optional<State> existState = masterDAO.getStateRepo().findById(addressDto.getStateId());
        Optional<City> existCity = masterDAO.getCityRepo().findById(addressDto.getCityId());
        Optional<User> existUser = masterDAO.getUserRepository().findById(addressDto.getUserId());

        if (existCountry.isEmpty()) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "Country not found");
        } else if (existState.isEmpty()) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "State not found");
        } else if (existCity.isEmpty()) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "City not found");
        } else if (existUser.isEmpty()) {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "User not found");
        }

        State state = existState.get();
        City city = existCity.get();
        Country country = existCountry.get();

        // Validate: State belongs to Country
        if (state.getCountry().getCountryId() != country.getCountryId()) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, "State does not belong to the selected Country");
        }

        // Validate: City belongs to State
        if (city.getState().getStateId() != state.getStateId()) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, "City does not belong to the selected State");
        }

        // All checks passed, proceed to save
        Address toEntity = AddressDto.toEntity(addressDto, country, state, city, existUser.get());
        Address savedAddress = addressDAO.saveAddress(toEntity);

        return new ResponseModel(HttpStatus.OK, AddressDto.toDto(savedAddress), "Address saved successfully");
    }


    //GET ALL ADDRESS  ----Admin Side
    @Override
    public ResponseModel getAllAddress() {
        List<Address> existAddress = addressDAO.findAllByStatus(Status.ACTIVE);
        if (existAddress.isEmpty()) {
            return  new ResponseModel(HttpStatus.NOT_FOUND,null,"Not Found");
        }else {
            List<AddressDto> dto =existAddress.stream()
                    .map(AddressDto::toDto)
                    .toList();
            return new ResponseModel(HttpStatus.OK,dto,"Success");
        }
    }

    //GET ADDRESS BY ID  ----User Side
    @Override
    public ResponseModel getAddressByUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Address> existAdd=addressDAO.findEmailAndStatus(email,Status.ACTIVE);
        if (existAdd != null){
            List<AddressDto> dto= existAdd.stream()
                    .map(AddressDto::toDto)
                    .toList();
            return new ResponseModel(HttpStatus.OK,dto,"Success");
        }
        return new ResponseModel(HttpStatus.NOT_FOUND,null,"Address Not Found");
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
            Optional<Address> existAddress = addressDAO.findByUserIdAndStatus(addressDto.getAddressId(), Status.ACTIVE);

            if (existAddress.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Address Not Found");
            }

            Address address = existAddress.get();

            // Load references only if IDs are provided
            Country country = null;
            if (addressDto.getCountryId() != null) {
                country = masterDAO.getCountryRepo().findById(addressDto.getCountryId()).orElse(null);
            }

            State state = null;
            if (addressDto.getStateId() != null) {
                state = masterDAO.getStateRepo().findById(addressDto.getStateId()).orElse(null);
            }

            City city = null;
            if (addressDto.getCityId() != null) {
                city = masterDAO.getCityRepo().findById(addressDto.getCityId()).orElse(null);
            }

            User user = null;
            if (addressDto.getUserId() != null) {
                user = masterDAO.getUserRepository().findById(addressDto.getUserId()).orElse(null);
            }

            // Update the address only with non-null values
            AddressDto.toUpdate(addressDto, address, country, state, city, user);

            Address savedAddress = addressDAO.saveAddress(address);
            return new ResponseModel(HttpStatus.OK, AddressDto.toDto(savedAddress), "Address Updated");

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Address Not Updated Due to an ERROR");
        }

    }




    //USER AND ADDRESS DETAILS  ----User Side
    @Override
    public ResponseModel UserDetailsWithAddress() {
        List<Object[]> user = addressDAO.findUsersAllDetailsByAddress();
        if (user.isEmpty()){
            return new ResponseModel(HttpStatus.NOT_FOUND,null,"User Details Not Found");
        }else {
            return new ResponseModel(HttpStatus.OK, user, "Success");
        }
    }


    //LIST OF COUNTRY  ----User Side
    @Override
    public ResponseModel ListOfAllCountry() {
        List<Country> countries = masterDAO.getCountryRepo().findAll();
        if(countries.isEmpty()){
            return new ResponseModel(HttpStatus.NOT_FOUND,null,"Country Not Found");
        }else {
            return new ResponseModel(HttpStatus.OK, countries, "Success");
        }
    }

    //GEY COUNTRY BY ID  ----User Side
    @Override
    public ResponseModel getAllCountryById(int id) {
        List<Country> countries=masterDAO.getCountryRepo().findAllByCountryId(id);
        if(countries.isEmpty()){
            return new ResponseModel(HttpStatus.NOT_FOUND,null,"Country Not Found");
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
            return new ResponseModel(HttpStatus.NOT_FOUND,null,"Country Not Exist");
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
            return new ResponseModel(HttpStatus.NOT_FOUND,null,"State Not Found");
        }
    }

    //User Profile  --User Side
    @Override
    public ResponseModel UserDetails() {
        List<UserProfileDto> user = addressDAO.findUser();
        if (user.isEmpty()){
            return  new ResponseModel(HttpStatus.NOT_FOUND,null,"Not Found");
        }else {
            return new ResponseModel(HttpStatus.OK, user, "Success");
        }
    }


}
