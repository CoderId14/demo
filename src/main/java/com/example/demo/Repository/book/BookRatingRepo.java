package com.example.demo.Repository.book;

import com.example.demo.criteria.BookPredicateBuilder;
import com.example.demo.entity.Category;
import com.example.demo.entity.Tag;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookRating;
import com.example.demo.entity.book.QBook;
import com.example.demo.entity.book.QBookRating;
import com.example.demo.exceptions.ParameterException;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BookRatingRepo extends JpaRepository<BookRating, Long>,
         QuerydslPredicateExecutor<BookRating>, QuerydslBinderCustomizer<QBookRating> , JpaSpecificationExecutor<Book> {
    @Override
    default void customize(QuerydslBindings bindings, QBookRating root) {
        if(root.book == null){
            throw new ParameterException("bookId");
        }
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
    }

}
