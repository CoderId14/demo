package com.example.demo.api.book;

import com.example.demo.Service.book.rating.BookRatingService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.book.request.rating.CreateBookRatingRequest;
import com.example.demo.api.book.request.rating.UpdateBookRatingRequest;
import com.example.demo.api.book.response.rating.BookRatingResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.book.BookRating;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-rating")
@RequiredArgsConstructor
public class BookRatingController {
    private final BookRatingService bookRatingService;

    @GetMapping("/v1/search")
    public ResponseEntity<?> searchBookRating(
            @QuerydslPredicate(root = BookRating.class) Predicate predicate,
            @PageableDefault(sort = "rating",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable
    ) {
        PagedResponse<BookRatingResponse> response = bookRatingService.searchBookRating(predicate, pageable);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addBookRating(
            @RequestBody CreateBookRatingRequest request,
            @CurrentUser CustomUserDetails currentUser
    ) {
        BookRatingResponse response = bookRatingService.addBookRating(request, currentUser);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/v1")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBookRating(
            @RequestBody UpdateBookRatingRequest request,
            @CurrentUser CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok().body(bookRatingService.updateBookRating(request, currentUser));
    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteBookRating(
            @PathVariable("id") long bookRatingId,
            @CurrentUser CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok().body(bookRatingService.deleteBookRating(bookRatingId, currentUser));
    }
}
