package com.example.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ObjectResponse {
    private HttpStatus status;
    private String message;
    private Object responseData;
}
