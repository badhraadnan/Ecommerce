package com.ecom.UserService.exception;

public class CityNotFound extends RuntimeException {
    public CityNotFound(String message) {
        super(message);
    }
}
