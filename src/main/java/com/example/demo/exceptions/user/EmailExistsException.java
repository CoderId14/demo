package com.example.demo.exceptions.user;


public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String message){
        super(message);
    }
}
