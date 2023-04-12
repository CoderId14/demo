package com.example.demo.api.book.response.rating;

import com.example.demo.dto.AbstractDTO;
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
    private long userId;
    private String name;
}
