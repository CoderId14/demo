package com.example.demo.Repository.book;

import com.example.demo.entity.book.BookRating;
import com.example.demo.entity.book.QBookRating;
import com.example.demo.exceptions.ParameterException;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingRepo extends JpaRepository<BookRating, Long>,
         QuerydslPredicateExecutor<BookRating>, QuerydslBinderCustomizer<QBookRating> , JpaSpecificationExecutor<BookRating> {
    @Override
    default void customize(QuerydslBindings bindings, QBookRating root) {
        if(root.book == null){
            throw new ParameterException("bookId");
        }
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    List<BookRating> findByBook_Id(long id);

    Optional<BookRating> findByUser_Id(long id);

    Optional<BookRating> findByBook_User_IdAndBook_Id(long userId, long bookId);



}
