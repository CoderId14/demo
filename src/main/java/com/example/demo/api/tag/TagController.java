package com.example.demo.api.tag;

import com.example.demo.Service.tag.TagService;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.tag.request.CreateTagRequest;
import com.example.demo.api.tag.request.UpdateTagRequest;
import com.example.demo.api.tag.response.TagResponse;
import com.example.demo.auth.CurrentUser;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.Tag;
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
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/v1/search")
    public ResponseEntity<PagedResponse<TagResponse>> searchTag(
            @QuerydslPredicate(root = Tag.class) Predicate predicate,
            @PageableDefault(sort = "createdDate",
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {

        PagedResponse<TagResponse> response = tagService.searchTag(predicate, pageable);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/v1/add")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addTag(@Validated @RequestBody CreateTagRequest tagRequest,
                                         @CurrentUser CustomUserDetails currentUser) {

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories").toUriString());
        TagResponse response = tagService.addTag(tagRequest, currentUser);
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> getTag(@PathVariable(name = "id") Long id) {
        TagResponse response = tagService.getTag(id);

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateTag(@PathVariable(name = "id") Long id,
                                            @Validated @RequestBody UpdateTagRequest request,
                                            @CurrentUser CustomUserDetails currentUser) {
        TagResponse response = tagService.updateTag(id, request, currentUser);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/categories/" + id).toUriString());
        return ResponseEntity.created(uri).body(response);

    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTag(@PathVariable(name = "id") Long id,
                                            @CurrentUser CustomUserDetails currentUser) {

        return new ResponseEntity<>(tagService.deleteTag(id, currentUser), HttpStatus.CREATED);
    }
}
