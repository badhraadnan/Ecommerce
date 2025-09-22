package com.ecom.UserService.advice;


import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserServiceControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseModel handleNotFoundException(UserNotFoundException userNotFoundException){
        System.out.println("Block in the Global Exception");
        return new ResponseModel(HttpStatus.NOT_FOUND,null, userNotFoundException.getMessage());
    }
}
