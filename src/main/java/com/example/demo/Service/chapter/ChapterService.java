package com.example.demo.Service.chapter;

import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.chapter.ChapterRepo;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Service.user.UserHistoryService;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.chapter.request.CreateChapterRequest;
import com.example.demo.api.chapter.request.UpdateChapterRequest;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.api.user.request.CreateUserBookHistoryRequest;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.book.Book;
import com.example.demo.exceptions.ParameterException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.demo.Utils.AppConstants.CHAPTER_NUMBER;

@AllArgsConstructor
@Transactional
@Service
public class ChapterService implements IChapterService {
    private final ChapterRepo chapterRepo;
    private final RoleUtils roleUtils;
    private final UserHistoryService userHistoryService;
    private BookRepo bookRepo;

    @Override
    public PagedResponse<ChapterResponse> searchChapter(Predicate predicate, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, CHAPTER_NUMBER);

        Page<Chapter> chapterPage = chapterRepo.findAll(predicate, pageable);

        List<Chapter> content = chapterPage.getNumberOfElements() == 0 ? Collections.emptyList() : chapterPage.getContent();

        List<ChapterResponse> chapterResponses = new ArrayList<>();
        content.forEach(category -> chapterResponses.add(getDtoFromEntity(category)));

        return new PagedResponse<>(chapterResponses, chapterPage.getNumber(), chapterPage.getSize(),
                chapterPage.getTotalElements(), chapterPage.getTotalPages(), chapterPage.isLast());
    }

    private ChapterResponse getDtoFromEntity(Chapter root) {
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

    @Override
    public ChapterResponse getChapter(Long id, CustomUserDetails currentUser) {
        Chapter chapter = chapterRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("chapter", "id", id)
        );
        if (currentUser != null) {
            userHistoryService.addUserBookHistory(CreateUserBookHistoryRequest.builder()
                    .userId(currentUser.getId())
                    .chapterId(id)
                    .bookId(chapter.getBook().getId())
                    .build(), currentUser);
        }

        return getDtoFromEntity(chapter);
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
        return getDtoFromEntity(chapter);

    }


    @Override
    public ChapterResponse updateChapter(Long id, UpdateChapterRequest request, CustomUserDetails currentUser) {
        Book book = bookRepo.findById(request.getBookId()).
                orElseThrow(() -> new ResourceNotFoundException("book", "id", request.getBookId()));
        Chapter chapter = chapterRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("chapter", "id", id));
        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);

        chapter.setTitle(request.getTitle());
        chapter.setChapterNumber(request.getChapterNumber());
        chapter.setContent(request.getContent());
        chapter.setDescription(request.getDescription());
        chapterRepo.save(chapter);
        return getDtoFromEntity(chapter);

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
