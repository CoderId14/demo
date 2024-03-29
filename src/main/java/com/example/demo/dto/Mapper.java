package com.example.demo.dto;


import com.example.demo.api.auth.response.JwtAuthenticationResponse;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.api.category.response.CategoryResponse;
import com.example.demo.api.chapter.response.ChapterContentResponse;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.api.tag.response.TagResponse;
import com.example.demo.api.user.response.UserResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.entity.Category;
import com.example.demo.entity.Role;
import com.example.demo.entity.Tag;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookLikeCount;
import com.example.demo.entity.book.BookRatingCount;
import com.example.demo.entity.book.BookViewCount;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.supports.ERole;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserRole;
import com.example.demo.exceptions.ParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class Mapper {
    public static UserResponse toUserDto(User user) {
        Set<ERole> roleSet = new HashSet<>();
        Set<UserRole> userRole = user.getUserRoles();
        userRole.stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .forEach(roleSet::add);

        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .roles(roleSet)
                .name(user.getName())
                .isActive(user.getIsActive())
                .coin(user.getCoin())
                .createDate(user.getCreatedDate())
                .modifyDate(user.getModifiedDate())
                .build();
    }

    public static JwtAuthenticationResponse toJwtAuthenticationRepsonse(String token, CustomUserDetails user, String refreshToken) {
        Set<ERole> roleSet = new HashSet<>();
        Set<UserRole> userRole = user.getUser().getUserRoles();
        userRole.stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .forEach(roleSet::add);
        return JwtAuthenticationResponse.builder()
                .accessToken(token)
                .username(user.getUsername())
                .refreshToken(refreshToken)
                .roles(roleSet)
                .build();
    }
    public static ChapterResponse getChapterResponseFromEntity(Chapter root) {
        if (root.getBook() == null) {
            throw new ParameterException("book");
        }

        return ChapterResponse.builder()
                .id(root.getId())
                .title(root.getTitle())
                .chapterNumber(root.getChapterNumber())
                .bookId(root.getBook().getId())
                .createdBy(root.getCreatedBy())
                .createdDate(root.getCreatedDate())
                .modifiedBy(root.getModifiedBy())
                .modifiedDate(root.getModifiedDate())
                .build();
    }
    public static ChapterContentResponse getChapterContentResponseFromEntity(Chapter root) {
        if (root.getBook() == null) {
            throw new ParameterException("book");
        }

        return ChapterContentResponse.builder()
                .id(root.getId())
                .title(root.getTitle())
                .content(root.getContent())
                .chapterNumber(root.getChapterNumber())
                .bookId(root.getBook().getId())
                .createdBy(root.getCreatedBy())
                .createdDate(root.getCreatedDate())
                .modifiedBy(root.getModifiedBy())
                .modifiedDate(root.getModifiedDate())
                .build();
    }
    public static TagResponse getTagResponseFromEntity(Tag root) {
        return TagResponse.builder()
                .tagId(root.getId())
                .tagName(root.getName())
                .description(root.getDescription())
                .modifiedDate(root.getModifiedDate())
                .build();
    }

    public static CategoryResponse getCategoryResponseFromEntity(Category root) {
        return CategoryResponse.builder()
                .categoryId(root.getId())
                .categoryName(root.getName())
                .description(root.getDescription())
                .modifiedDate(root.getModifiedDate())
                .build();
    }

    public static BookResponse getBookResponse(Book book, boolean isDetail) {
        String thumbnailUrl = book.getThumbnailUrl();

        List<ChapterResponse> latestChapter;
        long likeCount = Optional.ofNullable(book.getLikeCount())
                .map(BookLikeCount::getLikeCount)
                .orElse(0L);

        long viewCount = Optional.ofNullable(book.getViewCount())
                .map(BookViewCount::getViewCount)
                .orElse(0L);
        double averageRating = Optional.ofNullable(book.getBookRatingCount())
                .map(BookRatingCount::getAverageRating)
                .orElse(0d);
        long reviewCount = Optional.ofNullable(book.getBookRatingCount())
                .map(BookRatingCount::getRatingCount)
                .orElse(0L);
        Long chapterCount = Long.valueOf(Optional.ofNullable(book.getChapters())
                .map(List::size)
                .orElse(0));
        if(!isDetail){
            return BookResponse.builder()
                    .bookId(book.getId())
                    .title(book.getTitle())
                    .author(book.getUser().getName())
                    .thumbnailUrl(thumbnailUrl)
                    .viewCount(viewCount)
                    .likeCount(likeCount)
                    .reviewCount(reviewCount)
                    .averageRating(averageRating)
                    .isPremium(book.isPremium())
                    .isNovel(book.isNovel())
                    .totalChapter(chapterCount)
                    .isActive(book.getIsActive())
                    .build();
        }
        latestChapter = book.getChapters().stream()
                .sorted(Comparator.comparing(Chapter::getCreatedDate).reversed())
                .map(Mapper::getChapterResponseFromEntity)
                .limit(3)
                .toList();
        List<TagResponse> tagResponses = book.getTags().stream().map(Mapper::getTagResponseFromEntity).toList();
        List<CategoryResponse> categoryResponses = book.getCategories().stream().map(Mapper::getCategoryResponseFromEntity).toList();

        return BookResponse.builder()
                .bookId(book.getId())
                .title(book.getTitle())
                .content(book.getContent())
                .categories(categoryResponses)
                .tags(tagResponses)
                .shortDescription(book.getShortDescription())
                .author(book.getUser().getName())
                .thumbnailUrl(thumbnailUrl)
                .latestChapters(latestChapter)
                .likeCount(likeCount)
                .viewCount(viewCount)
                .reviewCount(reviewCount)
                .averageRating(averageRating)
                .isPremium(book.isPremium())
                .isNovel(book.isNovel())
                .totalChapter(chapterCount)
                .isActive(book.getIsActive())
                .build();

    }
}
