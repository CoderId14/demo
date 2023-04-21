package com.example.demo.api.book.response;

import com.example.demo.api.chapter.response.ChapterResponse;

import java.util.List;

public class BookWithLatestChapters {
    private Long id;
    private String title;
    private List<ChapterResponse> latestChapters;

    public BookWithLatestChapters(Long id, String title, List<ChapterResponse> latestChapters) {
        this.id = id;
        this.title = title;
        this.latestChapters = latestChapters;
    }
}
