package com.example.demo.Service.user;

import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.chapter.ChapterRepo;
import com.example.demo.Repository.user.UserBookHistoryRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.api.user.request.CreateUserBookHistoryRequest;
import com.example.demo.api.user.request.UserBookHistoryRequest;
import com.example.demo.api.user.response.UserBookHistoryResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserBookHistory;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.Utils.AppConstants.MODIFIED_DATE;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserHistoryService {

    private final UserBookHistoryRepo userBookHistoryRepo;

    private final UserRepo userRepo;

    private final BookRepo bookRepo;

    private final ChapterRepo chapterRepo;

    public PagedResponse<UserBookHistoryResponse> getHistory(UserBookHistoryRequest request, CustomUserDetails currentUser) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.Direction.DESC, MODIFIED_DATE);
        Page<UserBookHistory> userBookHistoryPage = userBookHistoryRepo.findBookReadingHistory(request.getUserId(), pageable);
        List<UserBookHistory> contents = userBookHistoryPage.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                userBookHistoryPage.getContent();
        List<UserBookHistoryResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getDtoResponse(temp)));

        return new PagedResponse<>(result,
                userBookHistoryPage.getNumber(),
                userBookHistoryPage.getSize(),
                userBookHistoryPage.getTotalElements(),
                userBookHistoryPage.getTotalPages(),
                userBookHistoryPage.isLast());
    }

    public UserBookHistoryResponse addUserBookHistory(CreateUserBookHistoryRequest request, CustomUserDetails currenUser) {

        long userId = request.getUserId();
        long bookId = request.getBookId();
        long chapterId = request.getChapterId();

        Book book = bookRepo.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException("book", "id", bookId)
        );
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user", "id", userId)
        );
        Chapter chapter = chapterRepo.findById(chapterId).orElseThrow(
                () -> new ResourceNotFoundException("chapter", "id", chapterId)
        );

        Optional<UserBookHistory> existHistory = userBookHistoryRepo.findByUser_IdAndBook_IdAndChapter_Id(request.getUserId(), request.getBookId(), request.getChapterId());

        if (existHistory.isPresent()) {
            if (existHistory.get().getUser().getId() != userId) {
                throw new UnauthorizedException("You don't have permisson to edit");
            }
            UserBookHistory userBookHistory = existHistory.get();
            userBookHistory.setModifiedDate(LocalDateTime.now());
            userBookHistory = userBookHistoryRepo.save(userBookHistory);
            return getDtoResponse(userBookHistory);
        }

        UserBookHistory userBookHistory = UserBookHistory.builder()
                .book(book)
                .user(user)
                .chapter(chapter)
                .build();
        userBookHistory = userBookHistoryRepo.save(userBookHistory);
        return getDtoResponse(userBookHistory);
    }

    public UserBookHistoryResponse getDtoResponse(UserBookHistory root) {
        User user = root.getUser();
        Book book = root.getBook();
        Chapter chapter = root.getChapter();
        return UserBookHistoryResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .chapterId(chapter.getId())
                .chapterTitle(chapter.getTitle())
                .chapterNumber(chapter.getChapterNumber())
                .createdDate(root.getCreatedDate())
                .modifiedDate(root.getModifiedDate())
                .build();
    }
}
