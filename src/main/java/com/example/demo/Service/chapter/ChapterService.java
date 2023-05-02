package com.example.demo.Service.chapter;

import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.chapter.ChapterRepo;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Service.user.UserHistoryService;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.chapter.request.CreateChapterRequest;
import com.example.demo.api.chapter.request.UpdateChapterRequest;
import com.example.demo.api.chapter.response.ChapterContentResponse;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.api.user.request.CreateUserBookHistoryRequest;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.book.Book;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.demo.dto.Mapper.getChapterContentResponseFromEntity;
import static com.example.demo.dto.Mapper.getChapterResponseFromEntity;

@AllArgsConstructor
@Transactional
@Service
public class ChapterService implements IChapterService {
    private final ChapterRepo chapterRepo;
    private final RoleUtils roleUtils;
    private final UserHistoryService userHistoryService;
    private BookRepo bookRepo;

    @Override
    public PagedResponse<ChapterResponse> searchChapter(Predicate predicate, Pageable pageable) {
        AppUtils.validatePageNumberAndSize(pageable.getPageNumber(), pageable.getPageSize());

        Page<Chapter> chapterPage = chapterRepo.findAll(predicate, pageable);

        List<Chapter> content = chapterPage.getNumberOfElements() == 0 ? Collections.emptyList() : chapterPage.getContent();

        List<ChapterResponse> chapterResponses = new ArrayList<>();
        content.forEach(category -> chapterResponses.add(getChapterResponseFromEntity(category)));
        long totalChapter = chapterRepo.findTotalChapter(predicate);
        return new PagedResponse<>(chapterResponses, chapterPage.getNumber(), chapterPage.getSize(),
                totalChapter, chapterPage.getTotalPages(), chapterPage.isLast());
    }


    @Override
    public ChapterContentResponse getChapter(Long id, CustomUserDetails currentUser) {
        Chapter chapter = chapterRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter", "id", id)
        );

        roleUtils.checkPremium(chapter.getBook().isPremium(), currentUser);

        if (currentUser != null) {
            userHistoryService.addUserBookHistory(CreateUserBookHistoryRequest.builder()
                    .userId(currentUser.getId())
                    .chapterId(id)
                    .bookId(chapter.getBook().getId())
                    .build(), currentUser);
        }

        return getChapterContentResponseFromEntity(chapter);
    }

    @Override
    public ChapterResponse addChapter(CreateChapterRequest request, CustomUserDetails currentUser) {
        Book book = bookRepo.findById(request.getBookId()).
                orElseThrow(() -> new ResourceNotFoundException("book", "id", request.getBookId()));
        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);

        int lastChapterNumber = chapterRepo.findLastChapterNumber(request.getBookId());
        Chapter chapter = Chapter.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .description(request.getDescription())
                .chapterNumber(lastChapterNumber + 1)
                .book(book)
                .build();
        chapter = chapterRepo.save(chapter);
        return getChapterResponseFromEntity(chapter);

    }


    @Override
    public ChapterResponse updateChapter(Long id, UpdateChapterRequest request, CustomUserDetails currentUser) {
        Chapter chapter = chapterRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", id));
        roleUtils.checkAuthorization(chapter.getBook().getCreatedBy(), currentUser);

        chapter.setTitle(request.getTitle());
        chapter.setChapterNumber(request.getChapterNumber());
        chapter.setContent(request.getContent());
        chapter.setDescription(request.getDescription());
        chapterRepo.save(chapter);
        return getChapterResponseFromEntity(chapter);

    }

    @Override
    public ApiResponse deleteChapter(Long id, CustomUserDetails currentUser) {
        Book book = bookRepo.findByChapters_Id(id).
                orElseThrow(() -> new ResourceNotFoundException("book", "chapterId", id));
        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);
        Chapter chapter = chapterRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("book", "chapterId", id));
        chapterRepo.delete(chapter);

        return new ApiResponse(Boolean.TRUE, "You successfully deleted chapter", HttpStatus.CREATED);

    }
}
