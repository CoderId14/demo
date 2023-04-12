package com.example.demo.api.book.request.rating;

import com.example.demo.entity.supports.RatingType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBookRatingRequest {
    private long userId;

    private long bookId;

    private String comment;

    private int rating;
}
