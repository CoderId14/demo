package com.example.demo.api.book.writer;


import com.example.demo.Service.book.BookService;
import com.example.demo.Service.book.writer.BookWriterService;
import com.example.demo.Service.book.writer.dto.response.BookWriterSearchResponse;
import com.example.demo.Service.book.writer.dto.response.WriterPromoteSearchResponse;
import com.example.demo.Utils.PageableUtils;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.writer.request.*;
import com.example.demo.api.book.writer.response.BookWriterResponse;
import com.example.demo.api.book.writer.response.WriterPromoteResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.CommonResponse;
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
    public ResponseEntity<?> searchBookWriterRequest(
            BookWriterSearchRequest request,
            @CurrentUser CustomUserDetails currentUser)
        {
        Pageable pageable = PageableUtils.generate(request.getPage(), request.getSize(),
                request.getSortBy() == null ? "-created_date" : request.getSortBy()
        );
        PagedResponse<BookWriterSearchResponse> response = bookWriterService.search(request.toInput(), pageable, currentUser);
        PagedResponse<BookWriterResponse> result = new PagedResponse<>(
                response.getContent().stream().map(BookWriterResponse::from).toList(),
                response.getPage(), response.getSize(),
                response.getTotalElements(), response.getTotalPages(),
                response.isLast());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/v1/search/promote")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> searchBookWriterRequestPromote(
            WriterPromoteSearchRequest request,
            @CurrentUser CustomUserDetails currentUser)
        {
        Pageable pageable = PageableUtils.generate(request.getPage(), request.getSize(),
                request.getSortBy() == null ? "-created_date" : request.getSortBy()
        );
        PagedResponse<WriterPromoteSearchResponse> response = bookWriterService.searchPromote(request.toInput(), pageable, currentUser);
        PagedResponse<WriterPromoteResponse> result = new PagedResponse<>(
                response.getContent().stream().map(WriterPromoteResponse::from).toList(),
                response.getPage(), response.getSize(),
                response.getTotalElements(), response.getTotalPages(),
                response.isLast());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(path = "/v1",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasAnyRole('ROLE_WRITER')")
    public ResponseEntity<?> addBookWriterRequest(
            @RequestBody @Validated CreateBookRequest model,
            @CurrentUser CustomUserDetails currentUser) {
        bookWriterService.saveBookForWriter(model, currentUser);
        return ResponseEntity.ok(CommonResponse.builder()
                        .code("200")
                        .content(null)
                .message("Success").build());
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBookWriter(
            @RequestBody @Validated BookWriterUpdateRequest request,
            @PathVariable("id") long id,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/v1" + id).toUriString());
        bookWriterService.updateStatusWriter(id, request.getStatus(), currentUser);
        return ResponseEntity.created(uri).body(
                CommonResponse.builder()
                        .code("200")
                        .content(null)
                        .message("Success").build());
    }

    @PostMapping(path = "/v1/promote",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ResponseEntity<?> addPromote(
            @RequestBody @Validated WriterPromoteCreateRequest request,
            @CurrentUser CustomUserDetails currentUser) {
        bookWriterService.saveWriterPromoteRequest(request, currentUser);
        return ResponseEntity.ok(CommonResponse.builder()
                .code("200")
                .content(null)
                .message("Success").build());
    }

    @PutMapping("/v1/promote/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePromoteStatus(
            @RequestBody @Validated WriterPromoteUpdateRequest request,
            @PathVariable("id") long id,
            @CurrentUser CustomUserDetails currentUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/book/v1" + id).toUriString());
        bookWriterService.updateStatusWriterPromote(id, request.getStatus(), currentUser);
        return ResponseEntity.created(uri).body(
                CommonResponse.builder()
                        .code("200")
                        .content(null)
                        .message("Success").build());
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
