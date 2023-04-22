package com.example.demo.entity.book;

import com.example.demo.entity.*;
import com.example.demo.entity.chapter.Chapter;
import com.example.demo.entity.user.User;
import com.example.demo.entity.user.UserBookHistory;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.modelmapper.ModelMapper;


@Entity
@Table(name = "tbl_book")
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book extends BaseEntity {

    @Column
    private String title;

    @Column
    private  String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_book_category",
            joinColumns = @JoinColumn(name ="book_id")
            , inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Category> categories;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<Chapter> chapters = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "thumbnail", referencedColumnName = "id")
    private Attachment thumbnail;
    @Column(columnDefinition = "TEXT")
    private String thumbnailUrl;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<UserBookHistory> userBookHistories;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookLike> bookLikes;
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookViewCount viewCount;
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookLikeCount likeCount;
    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookRatingCount bookRatingCount;
    @Column(name = "is_premium")
    private boolean isPremium;
    public Book(BookLike bookLike) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(bookLike.getBook(), this);
    }
}
