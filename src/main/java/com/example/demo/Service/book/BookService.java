package com.example.demo.Service.book;


import com.example.demo.Repository.BookLikeRepo;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.Service.attachment.AttachmentService;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.BookLike;
import com.example.demo.entity.Category;
import com.example.demo.entity.Tag;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.user.User;
import com.example.demo.exceptions.auth.UnauthorizedException;
import com.example.demo.exceptions.user.ResourceExistsException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static com.example.demo.dto.Mapper.getBookResponse;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService implements IBookService {

    private final BookRepo bookRepo;

    private final AttachmentService attachmentService;

    private final BookUtils bookUtils;

    private final RoleUtils roleUtils;

    private final BookLikeRepo bookLikeRepo;

    private final UserRepo userRepository;

    @Override
    public BookResponse save(CreateBookRequest request, CustomUserDetails currentUser) {
        Optional<Attachment> attachment = Optional.ofNullable(request.getThumbnail()).map(attachmentService::saveImg);

        Set<Category> categories = Optional.ofNullable(request.getCategories())
                .map(bookUtils::findSetCategory)
                .orElse(Collections.emptySet());

        Set<Tag> tags = Optional.ofNullable(request.getTags())
                .map(bookUtils::findSetTag)
                .orElse(Collections.emptySet());
        Book book;
        User author;
        if (request.getAuthor() != null) {
            author = userRepository.findById(request.getAuthor()).orElseThrow(
                    () -> new ResourceExistsException("User", "id", request.getAuthor())
            );
        } else {
            author = currentUser.getUser();
        }

        book = Book.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categories(categories)
                .shortDescription(request.getShortDescription())
                .thumbnail(attachment.orElse(null))
                .thumbnailUrl(request.getThumbnailUrl())
                .tags(tags)
                .user(author)
                .isPremium(request.getIsPremium())
                .isNovel(request.getIsNovel())
                .build();

        book = bookRepo.save(book);
        return getBookResponse(book, false);
    }

    @Override
    public BookResponse update(long id, UpdateBookRequest request, CustomUserDetails currentUser) {

        Set<Category> categories = bookUtils.findSetCategory(request.getCategories());

        Set<Tag> tags = bookUtils.findSetTag(request.getTags());

        Book book = bookUtils.findBookById(id);

        if (!currentUser.getId().equals(book.getUser().getId()) && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new UnauthorizedException("You don't have permission to do this");
        }
        User author;
        if(request.getAuthor() != null){
            author = userRepository.findById(request.getAuthor()).orElseThrow(
                    () -> new ResourceExistsException("User", "id", request.getAuthor())
            );
        } else{
            author = currentUser.getUser();
        }

        book.setTitle(request.getTitle());
        book.setContent(request.getContent());
        book.setThumbnailUrl(request.getThumbnailUrl());
        book.setPremium(request.getIsPremium());
        book.setNovel(request.getIsNovel());
        if(currentUser.getAuthorities().contains("ROLE_ADMIN")){
            book.setUser(author);
        }
        if (!categories.isEmpty()) book.setCategories(categories);
        if (!tags.isEmpty()) book.setTags(tags);

        book = bookRepo.save(book);

        return getBookResponse(book, false);

    }

    @Override
    public ApiResponse delete(long id, CustomUserDetails currentUser) {

        Book book = bookUtils.findBookById(id);
        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);
        bookRepo.deleteById(id);

        return new ApiResponse(Boolean.TRUE, "You successfully deleted post");

    }

    @Override
    public PagedResponse<BookResponse> getAllBooks(CustomUserDetails user, Pageable pageable) {
        AppUtils.validatePageNumberAndSize(pageable.getPageNumber(), pageable.getPageSize());

//        CategoryEntity category = categoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, id));

        Page<Book> books = bookRepo.findAll(pageable);
        return getBookResponsePagedResponse(books, user.getId(), pageable, false);
    }

    private PagedResponse<BookResponse> getBookResponsePagedResponse(Page<Book> books, Long userId, Pageable pageable, boolean isDetail) {
        List<Book> contents = books.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                books.getContent();

        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp, isDetail)));
        if (userId != null) {
            bookLikeRepo.findByUserIdAndBookIdIn(userId, contents.stream().map(Book::getId).toList())
                    .forEach(bookLike -> result.stream()
                            .filter(response -> response.getBookId() == bookLike.getBook().getId())
                            .findFirst()
                            .ifPresent(response -> response.setLiked(true)));
        }
        if (isDetail) {
            for (Sort.Order order : pageable.getSort()) {
                if (order.getProperty().equals("chapterModifiedDate")) {
                    Comparator<BookResponse> chapterModifiedDateComparator = getChapterModifiedDateComparator(order.getDirection());
                    result.sort(Comparator.comparing((BookResponse book) -> book.getLatestChapters().isEmpty())
                            .thenComparing(chapterModifiedDateComparator));
                }
            }
        }

        return new PagedResponse<>(result,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast());
    }

    private Comparator<BookResponse> getChapterModifiedDateComparator(Sort.Direction direction) {
        Comparator<BookResponse> comparator = Comparator.comparing(book ->
                        book.getLatestChapters().stream()
                                .map(ChapterResponse::getModifiedDate)
                                .max(Comparator.naturalOrder())
                                .orElse(null),
                Comparator.nullsLast(Comparator.naturalOrder()));
        if (direction == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    public PagedResponse<BookResponse> searchBook(Predicate predicate, Pageable pageable, CustomUserDetails currentUser, boolean isDetail) {
        Page<Book> books;
        books = bookRepo.searchBook(predicate, pageable);
        Long userId = currentUser != null ? currentUser.getId() : null;
        return getBookResponsePagedResponse(books, userId, pageable, isDetail);
    }

    public PagedResponse<BookResponse> findBookLikeByUserId(long id, Pageable pageable) {
        Page<BookLike> bookPage = bookLikeRepo.findByUserId(id, pageable);
        PageImpl<Book> books = new PageImpl(bookPage.stream()
                .map(BookLike::getBook)
                .toList());
        return getBookResponsePagedResponse(
                books, id, pageable, true);
    }


    public PagedResponse<BookResponse> hotBooks(CustomUserDetails user, Pageable pageable) {
        // thống kê danh sách
        Page<Book> bookPage = bookRepo.findTop100ByLikes(pageable);
        return getBookResponsePagedResponse(bookPage, user.getId(), pageable, false);
    }
}
