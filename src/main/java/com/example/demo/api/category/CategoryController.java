package com.example.demo.api.category;

import com.example.demo.Service.category.CategoryService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.category.request.CreateCategoryRequest;
import com.example.demo.api.category.request.UpdateCategoryRequest;
import com.example.demo.api.category.response.CategoryResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.Category;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/v1/search")
    public ResponseEntity<PagedResponse<CategoryResponse>> searchCategories(
            @QuerydslPredicate(root = Category.class) Predicate predicate,
            @PageableDefault(sort = "createdDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {

        PagedResponse<CategoryResponse> response = categoryService.searchCategory(predicate, pageable);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addCategory(@Validated @RequestBody CreateCategoryRequest categoryRequest,
                                         @CurrentUser CustomUserDetails currentUser) {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/category/v1/add").toUriString());
        CategoryResponse response = categoryService.addCategory(categoryRequest, currentUser);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getTag(@PathVariable(name = "id") Long id) {
        CategoryResponse response = categoryService.getCategory(id);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateTag(@PathVariable(name = "id") Long id,
                                            @Validated @RequestBody UpdateCategoryRequest request,
                                            @CurrentUser CustomUserDetails currentUser) {
        CategoryResponse response = categoryService.updateCategory(id, request, currentUser);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/" + id).toUriString());
        return ResponseEntity.created(uri).body(response);

    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTag(@PathVariable(name = "id") Long id,
                                            @CurrentUser CustomUserDetails currentUser) {

        return new ResponseEntity<>(categoryService.deleteCategory(id, currentUser), HttpStatus.CREATED);
    }
}
