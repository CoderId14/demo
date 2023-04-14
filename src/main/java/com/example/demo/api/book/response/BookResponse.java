package com.example.demo.api.book.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class BookResponse {
    private String title;

    private String content;

    private String shortDescription;

    private Set<String> categories;

    private Set<String> tags;

    private String thumbnail;

    private String thumbnailUrl;

    private String name;
}
