package com.example.demo.api.book.writer;


import com.example.demo.Service.book.BookService;
import com.example.demo.Service.book.writer.BookWriterService;
import com.example.demo.Utils.PageableUtils;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.api.book.writer.request.BookWriterSearchRequest;
import com.example.demo.api.book.writer.response.BookWriterResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/writer/book")
@RequiredArgsConstructor
public class BookWriterController {
    private final BookService bookService;
    private final BookWriterService bookWriterService;

    @GetMapping("/v1/search")
    public ResponseEntity<?> searchBook(
            BookWriterSearchRequest request,
            @CurrentUser CustomUserDetails currentUser)
        {
        Pageable pageable = PageableUtils.generate(request.getPage(), request.getSize(),
                request.getSortBy() == null ? "-created_date" : request.getSortBy()
        );
        PagedResponse<BookWriterResponse> response = bookWriterService.search(request.toInput(), pageable, currentUser);

        return ResponseEntity.ok().body(response);
    }


    @PostMapping(path = "/v1",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addBook(
            @RequestBody @Validated CreateBookRequest model,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/v1").toUriString());
        BookResponse response = bookService.save(model, currentUser);
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_WRITER') or hasRole('ROLE_ADMIN')")
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

}
