package com.example.demo.exceptions;


import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.ApiResponseError;
import com.example.demo.dto.response.ErrorResponse;
import com.example.demo.exceptions.user.AccountActiveException;
import com.example.demo.exceptions.user.ResourceExistsException;
import com.example.demo.exceptions.user.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<?> resolveException(TokenInvalidException ex){
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AccountActiveException.class)
    public ResponseEntity<?> resolveException(AccountActiveException ex){
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> resolveException(AuthenticationException ex){
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(BadRequestException.class)
//    @ResponseBody
//    public ResponseEntity<?> handleMethodArgumentNotValidException(BadRequestException ex) {
//        ApiResponseError apiResponseError = new ApiResponseError(ex.getMessage())
//    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resolveException(ResourceNotFoundException ex){
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<?> resolveException(ResourceExistsException ex){
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> resolveException(BindException ex){
        log.info("BindException");
        Map<String, String> errors = new HashMap<>();
        if(ex.getBindingResult().hasErrors()){

            ex.getBindingResult().getFieldErrors().forEach(
                    error -> errors.put(error.getField(),error.getDefaultMessage())
            );
            List<String> errorMessage = new ArrayList<>();
            ApiResponseError errorMessages = new ApiResponseError(new ArrayList<>());
            for (String key :
                    errors.keySet()) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                        .error(key)
                        .message(errors.get(key))
                        .build();
                errorMessages.getErrors().add(errorResponse);
//                errorMessage.add("Field: " + key + ", " + errors.get(key));
            }
//            ApiResponse apiResponse = new ApiResponse(false, errorMessage);
            return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);

        }
        log.info("Field valid");


        return new ResponseEntity<>("Field valid", HttpStatus.ACCEPTED);
    }
}
