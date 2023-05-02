package com.example.demo.Repository.chapter;

import com.example.demo.entity.chapter.Chapter;

import com.example.demo.entity.chapter.QChapter;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ChapterRepo extends JpaRepository<Chapter, Long>,
        ChapterRepoCustom, QuerydslPredicateExecutor<Chapter>, QuerydslBinderCustomizer<QChapter> {
    @Override
    default void customize(QuerydslBindings bindings, QChapter root) {
        bindings.bind(root.book.id).first(
                (path, value) -> path.eq(value));
        bindings.bind(root.chapterNumber).first(
                (path, value) -> path.eq(value));
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.createdBy);
        bindings.excluding(root.modifiedBy);
    }
    Page<Chapter> findByBookId(Long id, Pageable pageable);

    @Query("SELECT count(b.id) FROM Book b")
    long findTotalChapter(long bookId);
}
