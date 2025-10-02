package com.ecom.productservice.controller;

import com.ecom.productservice.exception.CartNotFound;
import com.ecom.productservice.exception.CategoryNotFound;
import com.ecom.productservice.exception.ProductNotFound;
import com.ecom.productservice.exception.UserNotFound;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ProductNotFound.class)
    public ProductNotFound handleProductNotFound(ProductNotFound ex) {
        return new ProductNotFound(ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFound.class)
    public CategoryNotFound handleCategoryNotFound(CategoryNotFound ex) {
        return new CategoryNotFound(ex.getMessage());
    }
    @ExceptionHandler(UserNotFound.class)
    public UserNotFound handleUserNotFound(UserNotFound ex) {
        return new UserNotFound(ex.getMessage());
    }
    @ExceptionHandler(CartNotFound.class)
    public CartNotFound handleCartNotFound(CartNotFound ex) {
        return new CartNotFound(ex.getMessage());
    }
}
