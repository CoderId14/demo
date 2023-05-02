package com.example.demo.api.book;


import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Service.book.BookService;
import com.example.demo.Service.book.likeCount.BookLikeCountService;
import com.example.demo.Service.book.rating.BookRatingCountService;
import com.example.demo.Service.book.viewCount.BookViewCountService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.book.request.BookSearchRequest;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.criteria.SearchCriteria;
import com.example.demo.criteria.rsql.CustomRsqlVisitor;
import com.example.demo.criteria.spec.book.BookSpecificationBuilder;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookLikeCount;
import com.example.demo.entity.book.BookRatingCount;
import com.example.demo.entity.book.BookViewCount;
import com.querydsl.core.types.Predicate;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    private final BookRepo bookRepo;

    private final BookViewCountService bookViewCountService;

    private final BookLikeCountService bookLikeCountService;

    private final BookRatingCountService bookRatingCountService;

    @GetMapping("/v1")
    public ResponseEntity<?> getAllBooks(
            @PageableDefault(sort = "createdDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable
    ) {
        PagedResponse<BookResponse> response = bookService.getAllBooks(pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/v1/search")
    public ResponseEntity<?> searchBook(
            @QuerydslPredicate(root = Book.class) Predicate predicate,
            @CurrentUser CustomUserDetails currentUser,
            @PageableDefault(sort = "chapterModifiedDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable,
            @RequestParam(value = "detail", required = false, defaultValue = "false") String detail
    ) {
//        chapterModifiedDate sort dành cho chapter
        if (!pageable.getSort().isUnsorted()) {
            String[] sortFields = {"chapterModifiedDate", "modifiedDate", "createdDate"}; // valid sort fields
            for (Sort.Order order : pageable.getSort()) {
                if (!Arrays.asList(sortFields).contains(order.getProperty())) {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid sort field: " + order.getProperty()));
                }
            }
        }

        boolean isDetail = detail.equals("true");
        PagedResponse<BookResponse> response = bookService.searchBook(predicate, pageable, currentUser, isDetail);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/test1")
    public List<Book> findAllByRsql(@RequestParam(value = "search") String search) {
        Node rootNode = new RSQLParser().parse(search);
        Specification<Book> spec = rootNode.accept(new CustomRsqlVisitor<Book>());
        return bookRepo.findAll(spec);
    }

    @PostMapping("/test2")
    public ResponseEntity<?> findAllWithSpec(
            @PageableDefault(sort = "createdDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable,
            @RequestBody BookSearchRequest request) {
        BookSpecificationBuilder builder = new BookSpecificationBuilder();
        List<SearchCriteria> criteriaList = request.getSearchCriteriaList();

        if (criteriaList != null) {
            criteriaList.forEach(x ->
            {
                x.setDataOption(request
                        .getDataOption());
                builder.with(x);
            });
        }

        Page<Book> result = bookRepo.findAll(builder.build(), pageable);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "/v1",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addBook(
            @RequestBody @Validated CreateBookRequest model,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/v1").toUriString());
        BookResponse response = bookService.save(model, currentUser);
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBook(
            @RequestBody UpdateBookRequest model,
            @PathVariable("id") long id,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/v1" + id).toUriString());
        return ResponseEntity.created(uri).body(
                bookService.update(id, model, currentUser));
    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteBook(
            @PathVariable(name = "id") long id,
            @CurrentUser CustomUserDetails currentUser) {
        ApiResponse apiResponse = bookService.delete(id, currentUser);
        return ResponseEntity.ok().body(apiResponse);

    }

    @GetMapping("/v1/likeCount")
    public ResponseEntity<?> hotBooksByLikeCount(
            @QuerydslPredicate(root = BookLikeCount.class) Predicate predicate,
            @PageableDefault(sort = "likeCount",
                    direction = Sort.Direction.DESC,
                    size = 10) Pageable pageable
    ) {
        PagedResponse<BookResponse> response = bookLikeCountService.getHotBookByLikeCount(predicate, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/v1/viewCount")
    public ResponseEntity<?> hotBooksByViewCount(
            @QuerydslPredicate(root = BookViewCount.class) Predicate predicate,
            @PageableDefault(sort = "viewCount",
                    direction = Sort.Direction.DESC,
                    size = 10) Pageable pageable
    ) {
        PagedResponse<BookResponse> response = bookViewCountService.getHotBookByViewCount(predicate, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/v1/ratingCount")
    public ResponseEntity<?> hotBooksByRatingCount(
            @QuerydslPredicate(root = BookRatingCount.class) Predicate predicate,
            @PageableDefault(sort = "averageRating",
                    direction = Sort.Direction.DESC,
                    size = 10) Pageable pageable
    ) {
        PagedResponse<BookResponse> response = bookRatingCountService.searchBookByRatingCount(predicate , pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/v1/testUpdateViewCount")
    public ResponseEntity<?> test(
    ) {
        bookViewCountService.updateViewCount();

        return ResponseEntity.ok().body("test");
    }

    @GetMapping("/v1/testUpdateLikeCount")
    public ResponseEntity<?> test1(
    ) {
        bookLikeCountService.updateLikeCount();

        return ResponseEntity.ok().body("test");
    }

    @GetMapping("/v1/testUpdateRatingCount")
    public ResponseEntity<?> test2(
    ) {
        bookRatingCountService.updateRatingCount();

        return ResponseEntity.ok().body("test");
    }

    public enum BookSortField {
        TITLE,
        AUTHOR,
        CREATED_DATE,
        MODIFIED_DATE,
        CHAPTER_UPDATE
    }

    public enum SortDirection {
        ASC,
        DESC
    }
}
