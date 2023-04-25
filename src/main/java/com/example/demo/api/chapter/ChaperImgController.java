package com.example.demo.api.chapter;

import com.example.demo.Service.chapter.ChapterImgService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.chapter.request.CreateChapterImgRequest;
import com.example.demo.api.chapter.request.UpdateChapterImgRequest;
import com.example.demo.api.chapter.response.ChapterImgResponse;
import com.example.demo.api.chapter.response.CreateChapterImgResponse;
import com.example.demo.api.chapter.response.ImgChapter;
import com.example.demo.api.chapter.response.UpdateChapterImgResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.entity.chapter.ChapterImg;
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
@RequestMapping("/api/chapterImg")
@RequiredArgsConstructor
public class ChaperImgController {

    private final ChapterImgService chapterImgService;


    @GetMapping("/v1/search/{chapterId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ChapterImgResponse> searchChapterImg(
            @CurrentUser CustomUserDetails currentUser,
            @PathVariable(name = "chapterId") Long chapterId,
            @PageableDefault(sort = "imgNumber",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {
        ChapterImgResponse response = chapterImgService.searchChapterImg(chapterId, pageable, currentUser);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addChapterImg(@Validated @RequestBody CreateChapterImgRequest request,
                                           @CurrentUser CustomUserDetails currentUser) {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/chapterImg/v1/add").toUriString());
        CreateChapterImgResponse response = chapterImgService.addChapterImg(request, currentUser);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getChapterImg(
            @QuerydslPredicate(root = ChapterImg.class) Predicate predicate) {
        ImgChapter response = chapterImgService.getChapterImg(predicate);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable(name = "id") String id,
                                    @Validated @RequestBody UpdateChapterImgRequest request,
                                    @CurrentUser CustomUserDetails currentUser) {
        UpdateChapterImgResponse response = chapterImgService.updateChapterImg(id, request, currentUser);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/chapterImg/" + id).toUriString());
        return ResponseEntity.created(uri).body(response);

    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteChapterImg(@PathVariable(name = "id") String id,
                                              @CurrentUser CustomUserDetails currentUser) {

        return new ResponseEntity<>(chapterImgService.deleteChapterImg(id, currentUser), HttpStatus.CREATED);
    }
}
