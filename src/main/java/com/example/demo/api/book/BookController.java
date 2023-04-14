package com.example.demo.api.book;


import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Service.book.BookService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.api.book.request.BookSearchRequest;
import com.example.demo.criteria.spec.book.BookSpecificationBuilder;
import com.example.demo.criteria.SearchCriteria;
import com.example.demo.criteria.rsql.CustomRsqlVisitor;
import com.example.demo.dto.PagedResponse;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.entity.book.Book;
import com.querydsl.core.types.Predicate;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.demo.Utils.AppConstants.CREATED_DATE;

@RestController
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepo bookRepo;


    @GetMapping("/v1")
    public ResponseEntity<?> getAllBooks(
            @RequestParam(value = "page", required = false,defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false,defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        PagedResponse<BookResponse> response = bookService.getAllBooks(page, size);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/v1/search")
    public ResponseEntity<?> searchBook(
            @QuerydslPredicate(root = Book.class) Predicate predicate,
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "page and size must >=0"));
        }
        PagedResponse<BookResponse> response = bookService.searchBook(predicate, page, size);

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
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
            @RequestBody BookSearchRequest request) {
        BookSpecificationBuilder builder = new BookSpecificationBuilder();
        List<SearchCriteria> criteriaList = request.getSearchCriteriaList();

        if(criteriaList != null){
            criteriaList.forEach(x->
            {x.setDataOption(request
                    .getDataOption());
                builder.with(x);
            });
        }
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);

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
            @ModelAttribute @RequestBody @Validated CreateBookRequest model,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/v1").toUriString());
        BookResponse response = bookService.save(model,currentUser);
        return ResponseEntity.created(uri).body(response) ;
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBook(
            @RequestBody UpdateBookRequest model,
            @PathVariable("id") long id,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/new/"+id).toUriString());
        return ResponseEntity.created(uri).body(
                bookService.update(id,model,currentUser));
    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteBook(
            @PathVariable(name = "id") long id,
            @CurrentUser CustomUserDetails currentUser) {
        ApiResponse apiResponse = bookService.delete(id,currentUser);
        return ResponseEntity.ok().body(apiResponse);

    }
}
