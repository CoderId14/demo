package com.example.demo.Service.chapter;

import com.example.demo.api.chapter.request.CreateChapterRequest;
import com.example.demo.api.chapter.request.UpdateChapterRequest;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.querydsl.core.types.Predicate;

public interface IChapterService {
    PagedResponse<ChapterResponse> searchChapter(Predicate predicate, int page, int size);

    ChapterResponse getChapter(Long id);

    ChapterResponse addChapter(CreateChapterRequest request, CustomUserDetails currentUser);

    ChapterResponse updateChapter(Long id, UpdateChapterRequest request, CustomUserDetails currentUser);

    ApiResponse deleteChapter(Long id, CustomUserDetails currentUser);
}
