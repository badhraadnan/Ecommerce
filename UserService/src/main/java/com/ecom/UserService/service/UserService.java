package com.ecom.UserService.service;


import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface UserService {
    ResponseModel SignupUser(UserDto userDto);
    ResponseModel getAllUsers();
    ResponseModel updateUser(UserDto userDto);
    ResponseModel blockUser();
    ResponseModel userFindById();
    ResponseModel deleteUser();
    ResponseModel UserLogin(LoginDto loginDto);
}
