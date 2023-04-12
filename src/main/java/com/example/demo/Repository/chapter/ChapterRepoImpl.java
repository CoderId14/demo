package com.example.demo.Repository.chapter;

import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.QChapter;
import com.example.demo.entity.book.QBook;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class ChapterRepoImpl implements ChapterRepoCustom {

    private final EntityManager em;

    public ChapterRepoImpl(EntityManager em) {

        this.em = em;
    }

    @Override
    public int findLastChapterNumber(Long bookId) {
        JPAQuery<Chapter> queryFactory = new JPAQuery<>(em);
        QChapter chapter = QChapter.chapter;
        QBook book = QBook.book;
        List<Integer> list = queryFactory
                .select(chapter.chapterNumber)
                .from(chapter)
                .innerJoin(chapter.book, book)
                .where(book.id.eq(bookId))
                .orderBy(chapter.chapterNumber.desc())
                .limit(1)
                .fetch();
        int lastChapterNumber = 0;
        if (list.size() > 0) {
            lastChapterNumber = list.get(0);
        }
        return lastChapterNumber;
    }
}
