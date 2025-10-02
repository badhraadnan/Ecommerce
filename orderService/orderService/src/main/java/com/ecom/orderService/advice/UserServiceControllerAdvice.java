package com.ecom.orderService.advice;


import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.orderService.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserServiceControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseModel handleNotFoundException(UserNotFoundException userNotFoundException) {
        return new ResponseModel(HttpStatus.NOT_FOUND, null, userNotFoundException.getMessage());
    }

    @ExceptionHandler(AddressNotFound.class)
    public ResponseModel handleAddressNotFoundException(AddressNotFound addressNotFound) {
        return new ResponseModel(HttpStatus.NOT_FOUND, null, addressNotFound.getMessage());
    }

    @ExceptionHandler(OrderNotFound.class)
    public ResponseModel handleOrderNotFoundException(OrderNotFound orderNotFound) {
        return new ResponseModel(HttpStatus.NOT_FOUND, null, orderNotFound.getMessage());
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseModel handleProductNotFoundException(ProductNotFound productNotFound) {
        return new ResponseModel(HttpStatus.NOT_FOUND, null, productNotFound.getMessage());
    }
    @ExceptionHandler(CartNotFound.class)
    public CartNotFound handleCartNotFound(CartNotFound ex) {
        return new CartNotFound(ex.getMessage());
    }
}
