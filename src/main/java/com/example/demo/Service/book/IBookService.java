package com.example.demo.Service.book;

import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.api.book.response.BookResponse;
import org.springframework.data.domain.Pageable;

public interface IBookService {
    BookResponse save(CreateBookRequest request, CustomUserDetails currentUser);

    BookResponse update(long id, UpdateBookRequest request, CustomUserDetails currentUser);

    ApiResponse delete(long id, CustomUserDetails currentUser);


    PagedResponse<BookResponse> getAllBooks(CustomUserDetails user, Pageable pageable);
}
