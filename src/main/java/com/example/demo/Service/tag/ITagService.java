package com.example.demo.Service.tag;

import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.TagDTO;
import com.example.demo.dto.response.ApiResponse;

public interface ITagService {

    PagedResponse<TagDTO> getAllTags(int page, int size);

    TagDTO getTag(Long id);

    TagDTO addTag(TagDTO Tag, CustomUserDetails currentUser);

    TagDTO updateTag(Long id, TagDTO newTag, CustomUserDetails currentUser);

    ApiResponse deleteTag(Long id, CustomUserDetails currentUser);
}
