package com.example.demo.Service.book.rating;

import com.example.demo.Repository.book.BookRatingCountRepo;
import com.example.demo.Repository.book.BookRatingRepo;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookRating;
import com.example.demo.entity.book.BookRatingCount;
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
public class BookRatingCountService {

    private final BookRepo bookRepo;

    private final BookRatingCountRepo bookRatingCountRepo;

    private final BookRatingRepo bookRatingRepo;

    public PagedResponse<BookResponse> searchBookByRatingCount(Predicate predicate, Pageable pageable){

        Page<BookRatingCount> books = bookRatingCountRepo.findAll(predicate, pageable);
        List<BookRatingCount> contents = books.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                books.getContent();

        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp.getBook(), false)));

        return new PagedResponse<>(result,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast());
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void updateRatingCount() {
        log.info("update rating count");
        List<Book> books = bookRepo.findAll();

        for(Book book: books){
            int total = 0;
            List<BookRating> ratings = bookRatingRepo.findByBook_Id(book.getId());
            for (BookRating rating : ratings) {
                total += rating.getRating();
            }
            double average = (double) total / ratings.size();
            if(Double.isNaN(average)){
                continue;
            }
            Optional<BookRatingCount> ratingCountOptional = bookRatingCountRepo.findByBook(book);
            BookRatingCount ratingCount;
            if (ratingCountOptional.isEmpty()) {
                ratingCount = BookRatingCount.builder()
                        .book(book)
                        .averageRating(average)
                        .build();
            } else {
                ratingCount = ratingCountOptional.get();
                ratingCount.setAverageRating(average);
            }
            bookRatingCountRepo.save(ratingCount);
        }


    }

}
