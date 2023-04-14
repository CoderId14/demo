package com.example.demo.Service.book;


import com.example.demo.Repository.BookLikeRepo;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.category.CategoryRepo;
import com.example.demo.Repository.tag.TagRepo;
import com.example.demo.Service.attachment.AttachmentService;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.Utils.AppUtils;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Attachment;
import com.example.demo.entity.BookLike;
import com.example.demo.entity.Category;
import com.example.demo.entity.Tag;
import com.example.demo.entity.book.Book;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.Utils.AppConstants.CREATED_DATE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService implements IBookService {

    private final BookRepo bookRepo;

    private final CategoryRepo categoryRepo;

    private final TagRepo tagRepo;

    private final AttachmentService attachmentService;

    private final BookUtils bookUtils;

    private final RoleUtils roleUtils;
    private final BookLikeRepo bookLikeRepo;

    private static BookResponse getBookResponse(Book book) {
        Optional<Attachment> thumbnail = Optional.ofNullable(book.getThumbnail());
        String thumbnailId = thumbnail.map(Attachment::getId).orElse(null);
        String thumbnailUrl = book.getThumbnailUrl();
        return BookResponse.builder()
                .title(book.getTitle())
                .content(book.getContent())
                .categories(book.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                .tags(book.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .shortDescription(book.getShortDescription())
                .name(book.getUser().getName())
                .thumbnail(thumbnailId)
                .thumbnailUrl(thumbnailUrl)
                .build();

    }

    @Override
    public BookResponse save(CreateBookRequest request, CustomUserDetails currentUser) {
        Optional<Attachment> attachment = Optional.ofNullable(request.getThumbnail()).map(attachmentService::saveImg);

        Set<Category> categories = Optional.ofNullable(request.getCategories())
                .map(bookUtils::findSetCategory)
                .orElse(Collections.emptySet());

        Set<Tag> tags = Optional.ofNullable(request.getTags())
                .map(bookUtils::findSetTag)
                .orElse(Collections.emptySet());

        Book book = Book.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categories(categories)
                .shortDescription(request.getShortDescription())
                .thumbnail(attachment.orElse(null))
                .thumbnailUrl(request.getThumbnailUrl())
                .tags(tags)
                .user(currentUser.getUser())
                .build();
        book = bookRepo.save(book);
        return getBookResponse(book);
    }

    @Override
    public BookResponse update(long id, UpdateBookRequest request, CustomUserDetails currentUser) {
        log.info("Update Book");

        Set<Category> categories = bookUtils.findSetCategory(request.getCategories());

        Set<Tag> tags = bookUtils.findSetTag(request.getTags());

        Book book = bookUtils.findBookById(id);

        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);

        book.setTitle(request.getTitle());
        book.setContent(request.getContent());

        if (!categories.isEmpty()) book.setCategories(categories);
        if (!tags.isEmpty()) book.setTags(tags);

        book = bookRepo.save(book);

        return getBookResponse(book);

    }

    @Override
    public ApiResponse delete(long id, CustomUserDetails currentUser) {

        Book book = bookUtils.findBookById(id);
        roleUtils.checkAuthorization(book.getCreatedBy(), currentUser);
        bookRepo.deleteById(id);

        return new ApiResponse(Boolean.TRUE, "You successfully deleted post");

    }

    @Override
    public PagedResponse<BookResponse> getAllBooks(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

//        CategoryEntity category = categoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);

        Page<Book> books = bookRepo.findAll(pageable);

        return getBookResponsePagedResponse(books);
    }

    private PagedResponse<BookResponse> getBookResponsePagedResponse(Page<Book> books) {
        List<Book> contents = books.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                books.getContent();

        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp)));

        return new PagedResponse<>(result,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast());
    }

    @Override
    public PagedResponse<BookResponse> getBooksByCategory(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);
        Page<Book> news = bookRepo.findByCategoriesIn(Collections.singleton(category), pageable);

        return getBookResponsePagedResponse(news);
    }

    @Override
    public PagedResponse<BookResponse> getBooksByTags(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Tag tag = tagRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("tag", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);

        Page<Book> books = bookRepo.findByTagsIn(Collections.singletonList(tag), pageable);

        return getBookResponsePagedResponse(books);
    }

    public PagedResponse<BookResponse> searchBook(Predicate predicate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);
        Page<Book> books = bookRepo.findAll(predicate, pageable);

        return getBookResponsePagedResponse(books);

    }
    public PagedResponse<BookResponse> findBookLikeByUserId(long id, Pageable pageable){
        Page<BookLike> bookPage = bookLikeRepo.findByUserId(id,pageable);
        return getBookResponsePagedResponse(
            new PageImpl<>(bookPage
                .stream()
                .map(Book::new)
                .collect(Collectors.toList()), bookPage.getPageable(), bookPage.getTotalElements()));
    }

    public void liked(long userid, long bookid) {
        BookLike bookLike = bookLikeRepo.findByUserIdAndAndBookId(userid,bookid).isPresent()?
        bookLikeRepo.findByUserIdAndAndBookId(userid,bookid).get(): null;
        if(bookLike != null){
            bookLikeRepo.save(bookLike);
        }else {
            log.info("invalid book or user!");
        }
    }
    public PagedResponse<BookResponse> hotBooks(int page, int size){
        // thống kê danh sách
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookPage = bookRepo.findTop100ByLikes(pageable);
        return getBookResponsePagedResponse(bookPage);
    }
}
