package com.ecom.UserService.service;


import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;

public interface UserService {
    ResponseModel getAllUsers();
    ResponseModel updateUser(UserDto userDto);
    ResponseModel blockUser(Long userId);
    ResponseModel userFindById(Long userId);
    ResponseModel deleteUser(Long userId);
}
