package com.example.demo.Service.book.viewCount;

import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.book.BookViewCountRepo;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookViewCount;
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
public class BookViewCountService {

    private final BookRepo bookRepo;

    private final BookViewCountRepo bookViewCountRepo;

    public PagedResponse<BookResponse> getHotBookByViewCount(Predicate predicate, Pageable pageable){

        Page<BookViewCount> books = bookViewCountRepo.findAll(predicate, pageable);
        List<BookViewCount> contents = books.getNumberOfElements() == 0 ?
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
    @Scheduled(cron = "0 0 0 * * *")
    public void updateViewCount() {
        log.info("update view count");
        List<Book> books = bookRepo.findAll();
        for (Book book : books) {
            Long viewCount = bookRepo.getViewCountForBook(book.getId());
            Optional<BookViewCount> bookViewCountOptional = bookViewCountRepo.findByBook(book);
            BookViewCount bookViewCount;
            if (bookViewCountOptional.isEmpty()) {
                bookViewCount = BookViewCount.builder()
                        .book(book)
                        .viewCount(viewCount)
                        .build();
            } else {
                bookViewCount = bookViewCountOptional.get();
                bookViewCount.setViewCount(viewCount);
            }
            bookViewCountRepo.save(bookViewCount);
        }
    }

}
