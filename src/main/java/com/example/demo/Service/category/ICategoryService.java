package com.example.demo.Service.category;


import com.example.demo.api.category.request.CreateCategoryRequest;
import com.example.demo.api.category.request.UpdateCategoryRequest;
import com.example.demo.api.category.response.CategoryResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {

    PagedResponse<CategoryResponse> searchCategory(Predicate predicate, Pageable pageable);

    CategoryResponse getCategory(Long id);

    CategoryResponse addCategory(CreateCategoryRequest request, CustomUserDetails currentUser);

    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request, CustomUserDetails currentUser);

    ApiResponse deleteCategory(Long id, CustomUserDetails currentUser);
}
