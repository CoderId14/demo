package com.example.demo.api.book.response.rating;

import com.example.demo.dto.AbstractDTO;
import com.example.demo.entity.book.BookRating;
import com.example.demo.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BookRatingResponse extends AbstractDTO {
    private long bookId;
    private String comment;
    private int rating;
    private long ratingId;
    private long userId;
    private String name;

    public static BookRatingResponse fromDTO(BookRating bookRating) {
        User user = bookRating.getUser();
        return BookRatingResponse.builder()
                .id(bookRating.getId())
                .userId(user.getId())
                .bookId(bookRating.getBook().getId())
                .name(user.getName())
                .ratingId(bookRating.getId())
                .comment(bookRating.getComment())
                .rating(bookRating.getRating())
                .createdBy(bookRating.getCreatedBy())
                .createdDate(bookRating.getCreatedDate())
                .modifiedBy(bookRating.getModifiedBy())
                .modifiedDate(bookRating.getModifiedDate())
                .build();
    }

}
