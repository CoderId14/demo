package com.example.demo.api.chapter.request;

import lombok.Data;

@Data
public class UpdateChapterRequest {
    private Long bookId;
    private Long id;
    private String title;
    private String content;
    private String description;
    private int chapterNumber;
}
