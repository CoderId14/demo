package com.example.demo.api.book;

import com.example.demo.Service.book.BookLikeService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.BookLike;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book-like")
@RequiredArgsConstructor
public class BookLikeController {
    private final BookLikeService bookLikeService;

    @GetMapping("/v1/search")
    public ResponseEntity<?> searchBookLike(
            @QuerydslPredicate(root = BookLike.class) Predicate predicate,
            @PageableDefault(sort = "modifiedDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable
    ) {
        PagedResponse<BookResponse> response = bookLikeService.searchBookLike(predicate, pageable);
        return ResponseEntity.ok().body(response);
    }

}
