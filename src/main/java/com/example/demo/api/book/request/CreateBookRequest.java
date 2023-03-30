package com.example.demo.api.book.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@Builder
public class CreateBookRequest {
    private MultipartFile thumbnail;
    private String title;
    private String shortDescription;
    private String content;
    private Set<String> categories;
    private Set<String> tags;
}
