package com.example.demo.Exception;

public class UserProfileNotFoundException extends RuntimeException {

    public UserProfileNotFoundException(Long id) {
        super("Could not find user profile " + id);
    }

}