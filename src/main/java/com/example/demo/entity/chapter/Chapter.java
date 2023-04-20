package com.example.demo.entity.chapter;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.ChapterComment;
import com.example.demo.entity.book.Book;
import com.example.demo.entity.user.UserBookHistory;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_chapter", indexes = @Index(name = "mdate_index", columnList = "modifiedDate"))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Builder
public class Chapter extends BaseEntity {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(columnDefinition = "TEXT")
    private String description;

    private int chapterNumber;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull
    private Book book;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<ChapterComment> chapterComment;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<UserBookHistory> userBookHistories;

}
