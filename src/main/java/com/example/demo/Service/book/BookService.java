package com.example.demo.Service.book;


import com.example.demo.Repository.*;
import com.example.demo.Repository.book.BookRepo;
import com.example.demo.Repository.tag.TagRepo;
import com.example.demo.Service.attachment.AttachmentService;
import com.example.demo.Utils.AppUtils;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.api.book.request.CreateBookRequest;
import com.example.demo.api.book.request.UpdateBookRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.api.book.response.BookResponse;
import com.example.demo.entity.*;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.auth.UnauthorizedException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.Utils.AppConstants.CREATED_DATE;
import static com.example.demo.entity.supports.ERole.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService implements IBookService {

    private final BookRepo bookRepo;

    private final CategoryRepo categoryRepo;

    private final TagRepo tagRepo;

    private final RoleRepo roleRepository;

    private final AttachmentService attachmentService;

    
    @Override
    public BookResponse save(CreateBookRequest request, CustomUserDetails currentUser) {

        Attachment attachment = null;

        if(request.getThumbnail() != null)
            attachment = attachmentService.saveImg(request.getThumbnail());

        Set<Category> categoryEntity = new HashSet<>();

        for (String name: request.getCategories()
             ) {
            categoryEntity.add(categoryRepo.findByName(name).orElseThrow(
                    () -> new ResourceNotFoundException("CATEGORY", "Name", name)
            ));
        }


        Set<Tag> tags = new HashSet<>();
        for (String title: request.getTags()
        ) {
            tags.add(tagRepo.findByTitle(title).orElseThrow(
                    () -> new ResourceNotFoundException("tag", "Title", title)
            ));
        }

        Book book = Book.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categories(categoryEntity)
                .shortDescription(request.getShortDescription())
                .thumbnail(attachment)
                .tags(tags)
                .user(currentUser.getUser())
                .build();
        book = bookRepo.save(book);
        return getBookResponse(book);
    }

    private static BookResponse getBookResponse(Book book) {
        Attachment thumbnail = book.getThumbnail();
        if (thumbnail != null){
            return BookResponse.builder()
                    .title(book.getTitle())
                    .content(book.getContent())
                    .categories(book.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                    .tags(book.getTags().stream().map(Tag::getTitle).collect(Collectors.toSet()))
                    .shortDescription(book.getShortDescription())
                    .thumbnail(book.getThumbnail().getId())
                    .username(book.getUser().getName())
                    .build();
        }
        return BookResponse.builder()
                        .title(book.getTitle())
                        .content(book.getContent())
                        .categories(book.getCategories().stream().map(Category::getName).collect(Collectors.toSet()))
                        .tags(book.getTags().stream().map(Tag::getTitle).collect(Collectors.toSet()))
                        .shortDescription(book.getShortDescription())
                        .username(book.getUser().getName())
                        .build();

    }

    @Override
    public BookResponse update(long id, UpdateBookRequest request, CustomUserDetails currentUser) {
        log.info("Update New");

        Set<Category> categories = new HashSet<>();

        for (String name: request.getCategories()
        ) {
            categories.add(categoryRepo.findByName(name).orElseThrow(
                    () -> new ResourceNotFoundException("CATEGORY", "Name", name)
            ));
        }


        Set<Tag> tags = new HashSet<>();
        for (String title: request.getTags()
        ) {
            tags.add(tagRepo.findByTitle(title).orElseThrow(
                    () -> new ResourceNotFoundException("tag", "Title", title)
            ));
        }


        Book book = bookRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("book", "id", id));

        if(book.getCreatedBy().equals(currentUser.getUsername())
        || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepository.findRoleByRoleName(ROLE_ADMIN).toString()))){


            book.setTitle(request.getTitle());
            book.setContent(request.getContent());

            if(!categories.isEmpty()) book.setCategories(categories);
            if(!tags.isEmpty()) book.setTags(tags);

            book = bookRepo.save(book);

            return getBookResponse(book);
        }
        ApiResponse apiResponse = new ApiResponse(
                Boolean.FALSE,
                "You don't have permission to edit this news");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(long id, CustomUserDetails currentUser) {

        Book book = bookRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("book", "id", id));
        if(book.getCreatedBy().equals(currentUser.getUsername())
                || currentUser.getAuthorities().contains(
                new SimpleGrantedAuthority(roleRepository.findRoleByRoleName(ROLE_ADMIN).toString()))){

            bookRepo.deleteById(id);

            return new ApiResponse(Boolean.TRUE, "You successfully deleted post");

        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this post");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PagedResponse<BookResponse> getAllBooks(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

//        CategoryEntity category = categoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException(CATEGORY, ID, id));

        Pageable pageable = PageRequest.of(page,size, Sort.Direction.DESC,CREATED_DATE);

        Page<Book> books = bookRepo.findAll(pageable);

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

        List<Book> contents = news.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                news.getContent();
        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp)));
        return new PagedResponse<>(result, news.getNumber(), news.getSize(), news.getTotalElements(),
                news.getTotalPages(), news.isLast());
    }

    @Override
    public PagedResponse<BookResponse> getBooksByTags(Long id, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Tag tag = tagRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("tag", "id", id));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);

        Page<Book> books = bookRepo.findByTagsIn(Collections.singletonList(tag), pageable);

        List<Book> contents = books.getNumberOfElements() == 0 ? Collections.emptyList() : books.getContent();

        List<BookResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(getBookResponse(temp)));
        return new PagedResponse<>(result,books.getNumber(), books.getSize(), books.getTotalElements(),
                books.getTotalPages(), books.isLast());
    }

    public PagedResponse<BookResponse> searchBook(Predicate predicate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_DATE);
        Page<Book> books = bookRepo.findAll(predicate, pageable);

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

}
