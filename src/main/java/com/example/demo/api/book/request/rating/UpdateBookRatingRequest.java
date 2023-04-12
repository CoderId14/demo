package com.example.demo.api.book.request.rating;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBookRatingRequest {
    private long ratingId;

    private String comment;

    private int rating;
}
