package com.example.demo.Repository.book;

import com.example.demo.entity.book.*;
import com.example.demo.exceptions.ParameterException;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRatingCountRepo extends JpaRepository<BookRatingCount, Long>,
        QuerydslPredicateExecutor<BookRatingCount>, QuerydslBinderCustomizer<QBookRatingCount>, JpaSpecificationExecutor<BookRatingCount> {
    @Override
    default void customize(QuerydslBindings bindings, QBookRatingCount root) {
        if(root.book.id == null){
            throw new ParameterException("bookId");
        }
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
    }
    Optional<BookRatingCount> findByBook(Book book);

}
