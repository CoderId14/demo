package com.example.demo.Service.book;

import com.example.demo.Repository.BookLikeRepo;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.entity.BookLike;
import com.example.demo.entity.Category;
import com.example.demo.entity.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookLikeService {
  private final BookLikeRepo bookLikeRepo;
  public List<BookResponse> findBookLikeByUserId(long id, Pageable pageable){
    return bookLikeRepo.findByUserId(id,pageable).map(booklike -> new BookResponse(booklike.getBook().getTitle(), booklike.getBook().getContent(),booklike.getBook().getShortDescription())).stream().toList();
  }

}
