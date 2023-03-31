package com.example.demo.api.tag.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTagRequest {
    private Long id;
    private String tagName;
    private String description;
}
