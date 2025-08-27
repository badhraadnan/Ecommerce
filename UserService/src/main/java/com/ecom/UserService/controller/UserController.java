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


    @GetMapping("/all-user")
    public ResponseModel getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/update")
    public ResponseModel updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @PatchMapping("/block/{userId}")
    public ResponseModel blockUser(@RequestParam Long userId){
        return userService.blockUser(userId);
    }

    @GetMapping("/get/{userId}")
    public ResponseModel getUserById(@RequestParam Long userId){
        return userService.userFindById(userId);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseModel deleteUser(@RequestParam Long userId){
        return userService.deleteUser(userId);
    }

}
