package com.example.demo.entity.book;


import com.example.demo.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Table(name = "tbl_book_like_count")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BookLikeCount extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private Book book;

    private Long likeCount;
}
