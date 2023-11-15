package com.example.demo.Service.category;

import com.example.demo.Repository.category.CategoryRepo;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.category.request.CreateCategoryRequest;
import com.example.demo.api.category.request.UpdateCategoryRequest;
import com.example.demo.api.category.response.CategoryResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Category;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.demo.dto.Mapper.getCategoryResponseFromEntity;

@AllArgsConstructor
@Transactional
@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepo categoryRepo;

    private final RoleUtils roleUtils;

    @Override
    public PagedResponse<CategoryResponse> searchCategory(Predicate predicate, Pageable pageable) {
        AppUtils.validatePageNumberAndSize(pageable.getPageNumber(), pageable.getPageSize());

        Page<Category> categoryPage = categoryRepo.findAll(predicate, pageable);

        List<Category> content = categoryPage.getNumberOfElements() == 0 ? Collections.emptyList() : categoryPage.getContent();

        List<CategoryResponse> categoryResponse = new ArrayList<>();
        content.forEach(category -> categoryResponse.add(getCategoryResponseFromEntity(category)));

        return new PagedResponse<>(categoryResponse, categoryPage.getNumber(), categoryPage.getSize(),
                categoryPage.getTotalElements(), categoryPage.getTotalPages(), categoryPage.isLast());
    }



    @Override
    public CategoryResponse getCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id)
        );
        return getCategoryResponseFromEntity(category);
    }

    @Override
    public CategoryResponse addCategory(CreateCategoryRequest request, CustomUserDetails currentUser) {
        Category category = new Category();
        category.setName(request.getCategoryName());
        category.setDescription(request.getDescription());
        categoryRepo.save(category);
        return getCategoryResponseFromEntity(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request, CustomUserDetails currentUser) {
        Category category = categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id));
        roleUtils.checkAuthorization(category.getCreatedBy(), currentUser);
        category.setName(request.getCategoryName());
        category.setDescription(request.getDescription());
        categoryRepo.save(category);
        return getCategoryResponseFromEntity(category);
    }

    @Override
    public ApiResponse deleteCategory(Long id, CustomUserDetails currentUser) {
        Category category = categoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("category", "id", id));
        roleUtils.checkAuthorization(category.getCreatedBy(), currentUser);
        categoryRepo.delete(category);
        return new ApiResponse(Boolean.TRUE, "You successfully deleted category", HttpStatus.CREATED);

    }
}
