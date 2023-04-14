package com.example.demo.entity;

import com.example.demo.entity.book.Book;
import com.example.demo.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tbl_book_like")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BookLike extends BaseEntity{
  @Column(name = "user_id", insertable = false, updatable = false)
  private long userId;
  @Column(name = "book_id", insertable = false, updatable = false)
  private long bookId;
  @ManyToOne( cascade = CascadeType.ALL)
  @JoinColumn(nullable = false,
      name = "user_id")
  private User user;
  @ManyToOne( cascade = CascadeType.ALL)
  @JoinColumn(nullable = false,
      name = "book_id")
  private Book book;

  public BookLike(long userId, long bookId) {
    this.userId = userId;
    this.bookId = bookId;
  }
}
