package com.example.demo.entity.book;


import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.supports.RatingType;
import com.example.demo.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Table(name = "tbl_book_rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BookRating extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int rating;

    private String comment;
}
