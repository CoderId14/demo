package com.example.demo.Service.book.rating;

import com.example.demo.Repository.book.BookRatingRepo;
import com.example.demo.Repository.user.UserRepo;
import com.example.demo.Service.book.BookUtils;
import com.example.demo.Service.role.RoleUtils;
import com.example.demo.api.book.request.rating.CreateBookRatingRequest;
import com.example.demo.api.book.request.rating.UpdateBookRatingRequest;
import com.example.demo.api.book.response.rating.BookRatingResponse;
import com.example.demo.auth.user.CustomUserDetails;
import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.book.BookRating;
import com.example.demo.entity.user.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.user.ResourceExistsException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.api.book.response.rating.BookRatingResponse.fromDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class BookRatingService {

    private final BookRatingRepo bookRatingRepo;

    private final UserRepo userRepo;

    private final BookUtils bookUtils;

    private final RoleUtils roleUtils;


    public PagedResponse<BookRatingResponse> searchBookRating(Predicate predicate, Pageable pageable){

        Page<BookRating> bookRatings = bookRatingRepo.findAll(predicate, pageable);
        List<BookRating> contents = bookRatings.getNumberOfElements() == 0 ?
                Collections.emptyList()
                :
                bookRatings.getContent();

        List<BookRatingResponse> result = new ArrayList<>();
        contents.forEach(temp -> result.add(fromDTO(temp)));

        return new PagedResponse<>(result,
                bookRatings.getNumber(),
                bookRatings.getSize(),
                bookRatings.getTotalElements(),
                bookRatings.getTotalPages(),
                bookRatings.isLast());
    }

    public BookRatingResponse addBookRating(CreateBookRatingRequest request, CustomUserDetails currentUser){
        roleUtils.checkAuthorization(currentUser.getUsername(), currentUser);
        User user =  currentUser.getUser();
        if(request.getUserId() != 0L){
            user =  userRepo.findById(request.getUserId()).
                    orElseThrow(() -> new ResourceNotFoundException("user", "id", request.getUserId()));
        }

        Book book = bookUtils.findBookById(request.getBookId());
        Optional<BookRating> existBookRating = bookRatingRepo.findByBook_User_IdAndBook_Id(user.getId(), request.getBookId());

        if(existBookRating.isPresent()){
            throw new ResourceExistsException("BookRating", "user", user.getId());
        }
        BookRating bookRating = BookRating.builder()
                .user(user)
                .book(book)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
        bookRating = bookRatingRepo.save(bookRating);
        return fromDTO(bookRating);
    }

    public BookRatingResponse updateBookRating(UpdateBookRatingRequest request, CustomUserDetails currentUser){
        BookRating bookRating =  bookRatingRepo.findById(request.getRatingId()).
                orElseThrow(() -> new ResourceNotFoundException("bookRating", "id", request.getRatingId()));
        roleUtils.checkAuthorization(bookRating.getCreatedBy(), currentUser);
        bookRating.setComment(request.getComment());
        bookRating.setRating(request.getRating());
        return fromDTO(bookRating);
    }

    public ApiResponse deleteBookRating(long id, CustomUserDetails currentUser){
        BookRating bookRating =  bookRatingRepo.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("bookRating", "id", id));
        roleUtils.checkAuthorization(bookRating.getCreatedBy(), currentUser);

        bookRatingRepo.delete(bookRating);
        return new ApiResponse(true, "delete book rating id = " + id +" successfully");
    }


}
