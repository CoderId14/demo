package com.example.demo.Repository.chapter;

import com.example.demo.entity.QChapter;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.chapter.ChapterImg;
import com.example.demo.entity.chapter.QChapterImg;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ChapterImgRepo extends JpaRepository<ChapterImg, Long>,
        QuerydslPredicateExecutor<ChapterImg>, QuerydslBinderCustomizer<QChapterImg> {
    @Override
    default void customize(QuerydslBindings bindings, QChapterImg root) {
        bindings.bind(root.chapter).first(
                (path, value) -> path.eq(value));
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    Page<ChapterImg> findByChapter_Id(long id, Pageable pageable);

}
