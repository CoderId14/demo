package com.example.demo.Repository.chapter;

import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.chapter.QChapter;
import com.example.demo.entity.chapter.QChapterImg;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class ChapterImgRepoImpl implements ChapterImgRepoCustom {

    private final EntityManager em;

    public ChapterImgRepoImpl(EntityManager em) {

        this.em = em;
    }

    @Override
    public int findLastChapterImgNumber(Long chapterId) {
        JPAQuery<Chapter> queryFactory = new JPAQuery<>(em);
        QChapter chapter = QChapter.chapter;
        QChapterImg chapterImg = QChapterImg.chapterImg;
        Integer lastImgNumber = queryFactory
                .select(chapterImg.imgNumber)
                .from(chapterImg)
                .innerJoin(chapterImg.chapter, chapter)
                .where(chapter.id.eq(chapterId))
                .orderBy(chapterImg.imgNumber.desc())
                .limit(1)
                .fetchOne();
        return lastImgNumber != null ? lastImgNumber : 0;
    }
}
