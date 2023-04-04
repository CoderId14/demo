package com.example.demo.entity.user;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.Book;
import com.example.demo.entity.Chapter;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user_book_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserBookHistory extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,
            name = "book_id")
    private Book book;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,
            name = "chapter_id")
    private Chapter chapter;
}
