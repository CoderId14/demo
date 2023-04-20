package com.example.demo.Service.tag;

import com.example.demo.api.tag.request.CreateTagRequest;
import com.example.demo.api.tag.request.UpdateTagRequest;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.api.tag.response.TagResponse;
import com.example.demo.dto.response.ApiResponse;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;

public interface ITagService {


    PagedResponse<TagResponse> searchTag(Predicate predicate, Pageable pageable);

    TagResponse getTag(Long id);

    TagResponse addTag(CreateTagRequest Tag, CustomUserDetails currentUser);

    TagResponse updateTag(Long id, UpdateTagRequest request, CustomUserDetails currentUser);

    ApiResponse deleteTag(Long id, CustomUserDetails currentUser);
}
