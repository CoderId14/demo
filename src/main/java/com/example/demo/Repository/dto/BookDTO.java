package com.example.demo.Repository.dto;

public interface BookDTO {
    Long getBookId();
    String getTitle();
    String getContent();
    String getShortDescription();
    String getThumbnail();
    String getThumbnailUrl();
    String getAuthor();
    long getViewCount();
    long getLikeCount();
    long getReviewCount();
    double getAverageRating();
    boolean isPremium();
    boolean isLiked();
    boolean isNovel();
    Long getTotalChapter();
}
