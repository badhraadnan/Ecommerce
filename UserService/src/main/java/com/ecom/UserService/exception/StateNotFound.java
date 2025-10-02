package com.ecom.UserService.exception;

public class StateNotFound extends RuntimeException {
    public StateNotFound(String message) {
        super(message);
    }
}
