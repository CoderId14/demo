package com.example.demo.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AccountActiveException extends RuntimeException{
    public AccountActiveException() {
        super("This account doesn't active, go to your email to active");
    }
}
