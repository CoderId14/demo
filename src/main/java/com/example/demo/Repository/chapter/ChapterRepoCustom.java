package com.example.demo.Repository.chapter;

import com.querydsl.core.types.Predicate;

public interface ChapterRepoCustom {
    int findLastChapterNumber(Long bookId);

    long findTotalChapter(Predicate predicate);
}
