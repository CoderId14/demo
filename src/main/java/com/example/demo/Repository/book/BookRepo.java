package com.example.demo.Repository.book;

import com.example.demo.criteria.BookPredicateBuilder;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.QBook;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Long>,
        BookRepoCustom, QuerydslPredicateExecutor<Book>, QuerydslBinderCustomizer<QBook>, JpaSpecificationExecutor<Book> {
    @Override
    default void customize(QuerydslBindings bindings, QBook root) {
        BookPredicateBuilder predicate = new BookPredicateBuilder();
        bindings.bind(root.tags).first(
                (path, values) -> {
                    predicate.withTags(values);
                    return predicate.build();
                });
        bindings.bind(root.categories).first(
                (path, values) -> {
                    predicate.withCategories(values);
                    return predicate.build();
                });
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(root.createdDate).all(
                (path, value) -> {
                    if (value.size() == 1)
                        return Optional.ofNullable(path.goe(value.iterator().next()));
                    Iterator<? extends LocalDateTime> it = value.iterator();
                    return Optional.ofNullable(path.between(it.next(), it.next()));
                });
        bindings.excluding(root.createdBy);
        bindings.excluding(root.modifiedBy);
    }

    Optional<Book> findByChapters_Id(long id);

    @Query("SELECT b FROM Book b JOIN b.bookLikes bl GROUP BY b.id ORDER BY COUNT(bl) DESC")
    Page<Book> findTop100ByLikes(Pageable pageable);

    @Query("SELECT COUNT(ur) FROM UserBookHistory ur WHERE ur.book.id = :bookId")
    Long getViewCountForBook(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(bl) FROM BookLike bl WHERE bl.book.id = :bookId")
    Long getLikeCountForBook(@Param("bookId") Long bookId);

}
