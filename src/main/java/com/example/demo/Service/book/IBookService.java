package com.example.demo.Service.book;

import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.api.book.response.BookResponse;

public interface IBookService {
    BookResponse save(CreateBookRequest request, CustomUserDetails currentUser);

    BookResponse update(long id, UpdateBookRequest request, CustomUserDetails currentUser);

    ApiResponse delete(long id, CustomUserDetails currentUser);

    PagedResponse<BookResponse> getAllBooks(int page, int size);

    PagedResponse<BookResponse> getBooksByCategory(Long id, int page, int size);

    PagedResponse<BookResponse> getBooksByTags(Long id, int page, int size);
}
