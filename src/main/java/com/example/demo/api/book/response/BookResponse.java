package com.example.demo.api.book.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
