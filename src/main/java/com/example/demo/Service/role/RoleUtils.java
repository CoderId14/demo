package com.example.demo.Service.role;

import com.example.demo.Repository.UserRoleRepo;
import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.role.response.RoleResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.user.UserRole;
import com.example.demo.exceptions.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.entity.supports.ERole.ROLE_ADMIN;
import static com.example.demo.entity.supports.ERole.ROLE_USER_VIP;
import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleUtils {

    private final UserRoleRepo userRoleRepo;

    private final RoleRepo roleRepo;

    public PagedResponse<RoleResponse> searchRole(Pageable pageable) {
        AppUtils.validatePageNumberAndSize(pageable.getPageNumber(), pageable.getPageSize());

        Page<Role> roles = roleRepo.findAll(pageable);

        List<Role> content = roles.getNumberOfElements() == 0 ? Collections.emptyList() : roles.getContent();

        List<RoleResponse> roleResponses = new ArrayList<>();
        content.forEach(role -> roleResponses.add(RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName().toString())
                .build()));
        return new PagedResponse<>(roleResponses, roles.getNumber(), roles.getSize(),
                roles.getTotalElements(), roles.getTotalPages(), roles.isLast());
    }

    public void checkAuthorization(String username, CustomUserDetails currentUser) {
        if (username.equals(currentUser.getUsername()) || currentUser.getAuthorities().stream().anyMatch(role ->
                role.getAuthority().equals(ROLE_ADMIN.toString()))) {
            return;
        }
        throw new UnauthorizedException("You don't have enough permission");
    }

    public void checkPremium(Book book, CustomUserDetails currentUser) {
        if (!book.isPremium()) {
            return;
        }
        if (currentUser.getUsername().equals(book.getCreatedBy()) || currentUser.getAuthorities().stream().anyMatch(role ->
                role.getAuthority().equals(ROLE_ADMIN.toString()))) {
            return;
        }
        Optional<UserRole> userRole = userRoleRepo.findByRole_RoleNameAndUser(ROLE_USER_VIP, currentUser.getUser());
        if (userRole.isPresent()) {

            if (userRole.get().getValidUntil().isBefore(now())) {
                throw new UnauthorizedException("You must purchase Premium to access");
            }
            return;
        }
        throw new UnauthorizedException("You don't have enough permission");
    }


}
