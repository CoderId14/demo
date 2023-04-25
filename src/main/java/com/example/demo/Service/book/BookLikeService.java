package com.example.demo.Service.book;

import com.example.demo.Repository.BookLikeRepo;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.BookLike;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.user.User;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.dto.Mapper.getBookResponse;

@Service
@AllArgsConstructor
public class BookLikeService {
    private final BookLikeRepo bookLikeRepo;

    private final UserRepo userRepo;

    private final BookRepo bookRepo;

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

    public PagedResponse<BookResponse> searchBookLike(Predicate predicate, Pageable pageable){

        Page<BookLike> bookRatings = bookLikeRepo.findAll(predicate, pageable);
        List<BookLike> contents = bookRatings.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                bookRatings.getContent();

        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp.getBook(), true)));

        return new PagedResponse<>(result,
                bookRatings.getNumber(),
                bookRatings.getSize(),
                bookRatings.getTotalElements(),
                bookRatings.getTotalPages(),
                bookRatings.isLast());
    }

    public void liked(long userId, long bookId) {
        Optional<BookLike> bookLikeOptional = bookLikeRepo.findByUserIdAndAndBookId(userId, bookId);
        if(bookLikeOptional.isPresent()){
            throw new BadRequestException("You already liked this book");
        }
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Book book = bookRepo.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("book", "id", bookId)
        );
        BookLike bookLike = BookLike.builder()
                .book(book)
                .user(user)
                .build();
        bookLikeRepo.save(bookLike);
    }

    public void unLiked(long userId, long bookId) {
        Optional<BookLike> bookLikeOptional = bookLikeRepo.findByUserIdAndAndBookId(userId, bookId);
        if(bookLikeOptional.isEmpty()){
            throw new RuntimeException("You already unLike this book " + bookId);
        }
        bookLikeRepo.delete(bookLikeOptional.get());
    }

}
