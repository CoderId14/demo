package com.example.demo.entity;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="tbl_book_comment")
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookComment extends BaseEntity{

    private String body = "Comment";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookComment that = (BookComment) o;
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
