package com.example.demo.api.chapter.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChapterRequest {
    private Long bookId;
    private String title;
    private String content;
    private String description;
}
