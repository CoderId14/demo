package com.example.demo.api.book.response;

import com.example.demo.api.category.response.CategoryResponse;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.api.tag.response.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private long bookId;
    private String title;

    private String content;

    private String shortDescription;

    private List<CategoryResponse> categories;

    private List<TagResponse> tags;

    private String thumbnail;

    private String thumbnailUrl;

    private String author;
    List<ChapterResponse> latestChapters;
    private long viewCount;
    private long likeCount;
    private double averageRating;
    private boolean isPremium;
}
