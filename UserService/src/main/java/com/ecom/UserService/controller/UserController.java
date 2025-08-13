package com.ecom.UserService.controller;

import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/service")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseModel saveUser(@RequestBody UserDto userDto){
    return userService.SignupUser(userDto);
    }

    @GetMapping("/")
    public ResponseModel getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/")
    public ResponseModel updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PatchMapping("/block")
    public ResponseModel blockUser(){
        return userService.blockUser();
    }

    @GetMapping("/get")
    public ResponseModel getUserById(){
        return userService.userFindById();
    }

    @DeleteMapping("/delete")
    public ResponseModel deleteUser(){
        return userService.deleteUser();
    }

    @PostMapping("/login")
    public ResponseModel LoginUser(@RequestBody LoginDto loginDto){
        return userService.UserLogin(loginDto);
    }

}
