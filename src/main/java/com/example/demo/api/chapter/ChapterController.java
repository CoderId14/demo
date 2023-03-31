package com.example.demo.api.chapter;

import com.example.demo.Service.chapter.ChapterService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.chapter.request.CreateChapterRequest;
import com.example.demo.api.chapter.request.UpdateChapterRequest;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.Chapter;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/chapter")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @GetMapping("/v1/search")
    public ResponseEntity<PagedResponse<ChapterResponse>> searchChapter(
            @QuerydslPredicate(root = Chapter.class) Predicate predicate,
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<ChapterResponse> response = chapterService.searchChapter(predicate, page, size);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addChapter(@Validated @RequestBody CreateChapterRequest request,
                                         @CurrentUser CustomUserDetails currentUser) {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories").toUriString());
        ChapterResponse response = chapterService.addChapter(request, currentUser);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getChapter(@PathVariable(name = "id") Long id) {
        ChapterResponse response = chapterService.getChapter(id);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateTag(@PathVariable(name = "id") Long id,
                                            @Validated @RequestBody UpdateChapterRequest request,
                                            @CurrentUser CustomUserDetails currentUser) {
        ChapterResponse response = chapterService.updateChapter(id, request, currentUser);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/" + id).toUriString());
        return ResponseEntity.created(uri).body(response);

    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteChapter(@PathVariable(name = "id") Long id,
                                            @CurrentUser CustomUserDetails currentUser) {

        return new ResponseEntity<>(chapterService.deleteChapter(id, currentUser), HttpStatus.CREATED);
    }
}
