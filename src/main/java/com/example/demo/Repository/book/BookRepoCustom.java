package com.example.demo.Repository.book;

import com.example.demo.entity.book.Book;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepoCustom {

    Page<Book> findAllBookAdvanced(Predicate predicate, Pageable pageable);

    Page<Book> searchBook(Predicate predicate, Pageable pageable);

}
