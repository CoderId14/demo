package com.example.demo.Repository.book;

import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookLikeCountRepo extends JpaRepository<BookLikeCount, Long> {

    Optional<BookLikeCount> findByBook(Book book);

}
