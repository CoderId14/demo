package com.example.demo.api.book.request;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateBookRequest {
    private String title;

    private String content;

    private  String shortDescription;

    private Set<String> categories;

    private Set<String> tags;
}
