package com.ecom.apiGateway.controller;

import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.apiGateway.service.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class authController {
    @Autowired
    private authService authService;

    @PostMapping("/signup")
    public ResponseModel saveUser(@RequestBody UserDto userDto) {
        return authService.SignupUser(userDto);
    }

    @PostMapping("/login")
    public ResponseModel LoginUser(@RequestBody LoginDto loginDto) {
        return authService.UserLogin(loginDto);

    }

    @PutMapping("/forgot-password")
    public ResponseModel forgotPassword(@RequestBody UserDto userDto){
        return authService.forgotPassword(userDto);
    }
}
