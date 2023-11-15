package com.example.demo.api.role;

import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Utils.AppConstants;
import com.example.demo.api.role.response.RoleResponse;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.Role;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleUtils roleUtils;


    @GetMapping("/v1/search")
    public ResponseEntity<PagedResponse<RoleResponse>> searchChapter(
            @QuerydslPredicate(root = Role.class) Predicate predicate,
            @PageableDefault(sort = AppConstants.CREATED_DATE,
                    direction = Sort.Direction.DESC,
                    size = AppConstants.DEFAULT_PAGE_SIZE) Pageable pageable) {

        PagedResponse<RoleResponse> response = roleUtils.searchRole(pageable);
        return ResponseEntity.ok().body(response);
    }

}
