package com.example.demo.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientEx extends RuntimeException{

  private static final long serialVersionUID = 1L;

  public InsufficientEx(String message) {
    super(message);
  }
}