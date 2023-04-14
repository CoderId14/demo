package com.example.demo.Service.book;

import com.example.demo.Repository.BookLikeRepo;
import com.example.demo.api.book.response.BookResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookLikeService {
    private final BookLikeRepo bookLikeRepo;

    public List<BookResponse> findBookLikeByUserId(long id, Pageable pageable) {
        return bookLikeRepo.findByUserId(id, pageable)
                .map(booklike ->
                         BookResponse.builder()
                                 .title(booklike.getBook().getTitle())
                                 .content(booklike.getBook().getContent())
                                 .shortDescription(booklike.getBook().getShortDescription())
                                 .build()
                ).stream().toList();
    }

}
