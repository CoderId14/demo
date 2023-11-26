package com.example.demo.Service.book.writer;


import com.example.demo.Repository.BookLikeRepo;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.book.writer.BookWriterRepo;
import com.example.demo.Repository.book.writer.dto.BookWriterDTO;
import com.example.demo.Repository.role.RoleRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.Repository.user.writer.UserPromoteRequestRepo;
import com.example.demo.Repository.user.writer.dto.UserPromoteDTO;
import com.example.demo.Service.attachment.AttachmentService;
import com.example.demo.Service.book.BookUtils;
import com.example.demo.Service.book.writer.dto.request.BookWriterCreateInput;
import com.example.demo.Service.book.writer.dto.request.BookWriterSearchInput;
import com.example.demo.Service.book.writer.dto.request.WriterPromoteSearchInput;
import com.example.demo.Service.book.writer.dto.response.BookWriterSearchResponse;
import com.example.demo.Service.book.writer.dto.response.WriterPromoteSearchResponse;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.api.book.writer.request.WriterPromoteCreateRequest;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Category;
import com.example.demo.entity.Role;
import com.example.demo.entity.Tag;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookWriterRequest;
import com.example.demo.entity.book.WriterRequestStatus;
import com.example.demo.entity.supports.ERole;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserPromoteRequest;
import com.example.demo.entity.user.UserRole;
import com.example.demo.exceptions.auth.UnauthorizedException;
import com.example.demo.exceptions.user.ResourceExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
public class BookWriterService {
    private final RoleRepo roleRepo;

    private final BookRepo bookRepo;

    private final AttachmentService attachmentService;

    private final BookUtils bookUtils;

    private final RoleUtils roleUtils;

    private final BookLikeRepo bookLikeRepo;

    private final UserRepo userRepository;

    private final BookWriterRepo bookWriterRepo;

    private final UserPromoteRequestRepo userPromoteRequestRepo;

    public PagedResponse<BookWriterSearchResponse> search(BookWriterSearchInput input, Pageable pageable, CustomUserDetails currentUser) {
        Page<BookWriterDTO> bookWriterDTOS;
        bookWriterDTOS = bookWriterRepo.search(
                input.getId(),
                input.getBookId(),
                input.getUserId(),
                input.getStatus(),
                pageable);
        Long userId = currentUser != null ? currentUser.getId() : null;
        if(!Objects.equals(userId, input.getUserId()) && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            throw new UnauthorizedException("You don't have permission to do this");
        }
        List<BookWriterSearchResponse> responses = bookWriterDTOS.getContent().stream().map(BookWriterSearchResponse::from).toList();

        PagedResponse<BookWriterSearchResponse> result = new PagedResponse<>(responses,
                bookWriterDTOS.getNumber(),
                bookWriterDTOS.getSize(),
                bookWriterDTOS.getTotalElements(),
                bookWriterDTOS.getTotalPages(),
                bookWriterDTOS.isLast());

        return result;
    }

    public PagedResponse<WriterPromoteSearchResponse> searchPromote(WriterPromoteSearchInput input, Pageable pageable, CustomUserDetails currentUser) {
        Page<UserPromoteDTO> userPromoteDTOS;
        userPromoteDTOS = userPromoteRequestRepo.search(
                input.getId(),
                input.getUserId(),
                input.getRoleId(),
                input.getStatus().toString(),
                pageable);
        List<WriterPromoteSearchResponse> responses = userPromoteDTOS.getContent().stream().map(WriterPromoteSearchResponse::from).toList();

        PagedResponse<WriterPromoteSearchResponse> result = new PagedResponse<>(responses,
                userPromoteDTOS.getNumber(),
                userPromoteDTOS.getSize(),
                userPromoteDTOS.getTotalElements(),
                userPromoteDTOS.getTotalPages(),
                userPromoteDTOS.isLast());

        return result;
    }
    public void saveBookWriterRequest(BookWriterCreateInput input, CustomUserDetails currentUser) {
        Book book = bookUtils.findBookById(input.getBookId());
        if(!Objects.equals(book.getUser().getId(), currentUser.getId()) && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            throw new UnauthorizedException("You don't have permission to do this");
        }
        User user = userRepository.findById(input.getUserId()).orElseThrow(
                () -> new ResourceExistsException("User", "id", input.getUserId())
        );
        BookWriterRequest bookWriterRequest = BookWriterRequest.builder()
                .book(book)
                .user(user)
                .status(input.getStatus())
                .build();
        bookWriterRepo.save(bookWriterRequest);
    }

    public void saveWriterPromoteRequest(WriterPromoteCreateRequest input, CustomUserDetails currentUser) {
        Role role = roleRepo.findRoleByRoleName(ERole.valueOf(input.getRoleName().toUpperCase())).orElseThrow(
                () -> new ResourceExistsException("Role", "name", input.getRoleName())
        );

        User user = userRepository.findById(input.getUserId()).orElseThrow(
                () -> new ResourceExistsException("User", "id", input.getUserId())
        );
        UserPromoteRequest userPromoteRequest = UserPromoteRequest.builder()
                .user(user)
                .role(role)
                .status(WriterRequestStatus.PENDING)
                .build();
        userPromoteRequestRepo.save(userPromoteRequest);
    }

    public void saveBookForWriter(CreateBookRequest request, CustomUserDetails currentUser) {
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
                .thumbnailUrl(request.getThumbnailUrl())
                .tags(tags)
                .user(author)
                .isPremium(request.getIsPremium())
                .isNovel(request.getIsNovel())
                .isActive(false)
                .build();

        book = bookRepo.save(book);

        saveBookWriterRequest(BookWriterCreateInput.builder()
                .bookId(book.getId())
                .userId(currentUser.getId())
                .status(WriterRequestStatus.PENDING)
                .build(), currentUser);
    }
    public void updateStatusWriter(Long id, WriterRequestStatus status, CustomUserDetails currentUser) {
        BookWriterRequest bookWriterRequest = bookWriterRepo.findById(id).orElseThrow(
                () -> new ResourceExistsException("BookWriterRequest", "id", id)
        );
        if(!Objects.equals(bookWriterRequest.getUser().getId(), currentUser.getId()) && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            throw new UnauthorizedException("You don't have permission to do this");
        }
        Book book = bookWriterRequest.getBook();
        bookWriterRequest.setStatus(status);
        if(status.equals(WriterRequestStatus.ACCEPTED)){
            book.setIsActive(true);
        } else if(status.equals(WriterRequestStatus.REJECTED)){
            book.setIsActive(false);
        }
        bookRepo.save(book);
        bookWriterRepo.save(bookWriterRequest);
    }

    public void updateStatusWriterPromote(Long id, WriterRequestStatus status, CustomUserDetails currentUser) {
        UserPromoteRequest userPromoteRequest = userPromoteRequestRepo.findById(id).orElseThrow(
                () -> new ResourceExistsException("userPromote", "id", id)
        );
        Role role = userPromoteRequest.getRole();
        User user = userPromoteRequest.getUser();

        userPromoteRequest.setStatus(status);

        if(status.equals(WriterRequestStatus.ACCEPTED)){
            UserRole userRole = UserRole.builder()
                .role(role)
                .build();
            userRole.setUser(user);
            user.getUserRoles().add(userRole);
            userRepository.save(user);
        }
        userPromoteRequestRepo.save(userPromoteRequest);
    }

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


    public ApiResponse delete(long id, CustomUserDetails currentUser) {

        Book book = bookUtils.findBookById(id);
        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);
        bookRepo.deleteById(id);

        return new ApiResponse(Boolean.TRUE, "You successfully deleted post");

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




}
