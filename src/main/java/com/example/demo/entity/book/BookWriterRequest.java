package com.example.demo.entity.book;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.user.User;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_book_writer_request")
public class BookWriterRequest extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    @Enumerated(EnumType.STRING)
    private WriterRequestStatus status;

}
