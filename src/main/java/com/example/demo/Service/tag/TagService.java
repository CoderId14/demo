package com.example.demo.Service.tag;

import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.Repository.tag.TagRepo;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.tag.request.CreateTagRequest;
import com.example.demo.api.tag.request.UpdateTagRequest;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.api.tag.response.TagResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Tag;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.auth.UnauthorizedException;
import com.example.demo.exceptions.user.ResourceExistsException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.dto.Mapper.getTagResponseFromEntity;
import static com.example.demo.entity.supports.ERole.ROLE_ADMIN;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService implements ITagService {

    private final TagRepo tagRepository;

    private final RoleRepo roleRepository;

    @Override
    public PagedResponse<TagResponse> searchTag(Predicate predicate, Pageable pageable) {
        AppUtils.validatePageNumberAndSize(pageable.getPageNumber(), pageable.getPageSize());

        Page<Tag> tagEntities = tagRepository.findAll(predicate, pageable);

        List<Tag> content = tagEntities.getNumberOfElements() == 0 ? Collections.emptyList() : tagEntities.getContent();

        List<TagResponse> categoryRespons = new ArrayList<>();
        content.forEach(tag -> categoryRespons.add(getTagResponseFromEntity(tag)));

        return new PagedResponse<>(categoryRespons, tagEntities.getNumber(), tagEntities.getSize(),
                tagEntities.getTotalElements(), tagEntities.getTotalPages(), tagEntities.isLast());
    }

    @Override
    public TagResponse getTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tag", "id", id)
        );
        return getTagResponseFromEntity(tag);
    }

    @Override
    public TagResponse addTag(CreateTagRequest request, CustomUserDetails currentUser) {
        Tag tag = new Tag();
        Optional<Tag> tagOptional = tagRepository.findByName(request.getTagName());
        if(tagOptional.isPresent()){
            throw new ResourceExistsException("tag", "name", request.getTagName());
        }
        tag.setName(request.getTagName());
        tag.setDescription(request.getDescription());
        tagRepository.save(tag);
        return getTagResponseFromEntity(tag);
    }

    @Override
    public TagResponse updateTag(Long id, UpdateTagRequest request, CustomUserDetails currentUser) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tag", "id", id));
        if(tag.getName().equals(request.getTagName())){
            throw new ResourceExistsException("tag", "name", request.getTagName());
        }
        if (tag.getCreatedBy().equals(currentUser.getUsername())
                || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepository.findRoleByRoleName(ROLE_ADMIN).toString()))) {
            tag.setName(request.getTagName());
            tag.setDescription(request.getDescription());
            tagRepository.save(tag);

            return getTagResponseFromEntity(tag);
        }
        throw new UnauthorizedException("You don't have permission to edit this tag");
    }

    @Override
    public ApiResponse deleteTag(Long id, CustomUserDetails currentUser) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tag", "id", id));
        if (tag.getCreatedBy().equals(currentUser.getUsername())
                || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepository.findRoleByRoleName(ROLE_ADMIN).toString()))) {
            tagRepository.delete(tag);

            return new ApiResponse(Boolean.TRUE, "You successfully deleted tag", HttpStatus.CREATED);
        }
        throw new UnauthorizedException("You don't have permission to edit this tag");
    }
}
