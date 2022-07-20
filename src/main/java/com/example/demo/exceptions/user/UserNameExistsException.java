package com.example.demo.exceptions.user;

public class UserNameExistsException extends RuntimeException {
    public UserNameExistsException(String message){
        super(message);
    }
}
