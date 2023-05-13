package com.example.demo.Service.book.likeCount;

import com.example.demo.Repository.book.BookLikeCountRepo;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookLikeCount;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.dto.Mapper.getBookResponse;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookLikeCountService {

    private final BookRepo bookRepo;

    private final BookLikeCountRepo bookLikeCountRepo;

    public PagedResponse<BookResponse> getHotBookByLikeCount(Predicate predicate, Pageable pageable){

        Page<BookLikeCount> books = bookLikeCountRepo.findAll(predicate, pageable);
        List<BookLikeCount> contents = books.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                books.getContent();

        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp.getBook(), true)));

        return new PagedResponse<>(result,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast());
    }
    @Scheduled(fixedDelayString = "60000")
    public void updateLikeCount() {
        log.info("update like count");
        List<Book> books = bookRepo.findAll();
        for (Book book : books) {
            Long likeCount = bookRepo.getLikeCountForBook(book.getId());
            Optional<BookLikeCount> bookViewCountOptional = bookLikeCountRepo.findByBook(book);
            BookLikeCount bookLikeCount;
            if (bookViewCountOptional.isEmpty()) {
                bookLikeCount = BookLikeCount.builder()
                        .book(book)
                        .likeCount(likeCount)
                        .build();
            } else {
                bookLikeCount = bookViewCountOptional.get();
                bookLikeCount.setLikeCount(likeCount);
            }
            bookLikeCountRepo.save(bookLikeCount);
        }
    }

}
