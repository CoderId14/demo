package com.example.demo.api.book.response;

import com.example.demo.entity.BookLike;
import lombok.AllArgsConstructor;
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

    private String name;

    public BookResponse(String title, String content, String shortDescription) {
        this.title = title;
        this.content = content;
        this.shortDescription = shortDescription;
    }

    public BookResponse(String title, String content, String shortDescription,
        Set<String> categories,
        Set<String> tags, String thumbnail, String name) {
        this.title = title;
        this.content = content;
        this.shortDescription = shortDescription;
        this.categories = categories;
        this.tags = tags;
        this.thumbnail = thumbnail;
        this.name = name;
    }

}
