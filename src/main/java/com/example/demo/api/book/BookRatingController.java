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
import com.example.demo.entity.supports.SortType;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.Utils.AppConstants.CREATED_DATE;

@RestController
@RequestMapping("/api/book-rating")
@RequiredArgsConstructor
public class BookRatingController {
    private final BookRatingService bookRatingService;

    @GetMapping("/v1/search")
    public ResponseEntity<?> searchBookRating(
            @QuerydslPredicate(root = BookRating.class) Predicate predicate,
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(value = "sortType", required = false, defaultValue = CREATED_DATE) SortType sortType,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        Pageable pageable = PageRequest.of(page, size, sortDirection, sortType.getSortType());
        PagedResponse<BookRatingResponse> response = bookRatingService.searchBookRating(predicate, pageable);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addBookRating(
            @RequestBody CreateBookRatingRequest request
    ) {
        BookRatingResponse response = bookRatingService.addBookRating(request);
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
