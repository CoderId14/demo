package com.example.demo.Service.tag;

import com.example.demo.Repository.RoleRepo;
import com.example.demo.Repository.tag.TagRepo;
import com.example.demo.Utils.AppUtils;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.TagDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Tag;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.demo.Utils.AppConstants.CREATED_DATE;
import static com.example.demo.entity.supports.ERole.ROLE_ADMIN;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService implements ITagService {

    private final TagRepo tagRepository;

    private final RoleRepo roleRepository;

    @Override
    public PagedResponse<TagDTO> getAllTags(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);

        Page<Tag> tagEntities = tagRepository.findAll(pageable);

        List<Tag> content = tagEntities.getNumberOfElements() == 0 ? Collections.emptyList() : tagEntities.getContent();

        List<TagDTO> tagDTOS = new ArrayList<>();
        content.forEach(tag -> tagDTOS.add(getDtoFromEntity(tag)));

        return new PagedResponse<>(tagDTOS, tagEntities.getNumber(), tagEntities.getSize(),
                tagEntities.getTotalElements(), tagEntities.getTotalPages(), tagEntities.isLast());
    }

    private TagDTO getDtoFromEntity(Tag tag) {
        return TagDTO.builder()
                .title(tag.getTitle())
                .content(tag.getContent())
                .build();
    }

    @Override
    public TagDTO getTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tag", "id", id)
        );
        return getDtoFromEntity(tag);
    }

    @Override
    public TagDTO addTag(TagDTO Tag, CustomUserDetails currentUser) {
        Tag tag = new Tag();
        tag.setTitle(tag.getTitle());
        tag.setContent(tag.getContent());
        tagRepository.save(tag);
        return getDtoFromEntity(tag);
    }

    @Override
    public TagDTO updateTag(Long id, TagDTO newTag, CustomUserDetails currentUser) {
        Tag tag = tagRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tag", "id", id));
        if (tag.getCreatedBy().equals(currentUser.getUsername())
                || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepository.findRoleByRoleName(ROLE_ADMIN).toString()))) {

            tag.setTitle(newTag.getTitle());
            tag.setContent(newTag.getContent());
            tagRepository.save(tag);

            return getDtoFromEntity(tag);
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
