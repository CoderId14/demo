package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonResponse {
    private String code;
    private String message;
    private Object content;
}
