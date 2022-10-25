package com.example.demo.exceptions;

import com.example.demo.dto.response.ApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
   private ApiResponseError errors;

   public BadRequestException(ApiResponseError errors){
       this.errors = errors;
   }
}
