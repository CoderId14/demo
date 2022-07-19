package com.example.demo.Entity;


import java.time.LocalDateTime;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "body", nullable = true, length = 5000)
    private String body;

    @Column(name = "path", nullable = true, length = 500)
    private String path;

    @ Default
    @Column(name = "deleted")
    private boolean deleted = false;

    @CreatedDate
    @Column(name = "created", nullable = true)
    private LocalDateTime created;

    @LastModifiedDate
    @Column(name = "modified", nullable = true)
    private LocalDateTime modified;

    @CreatedBy
    @Column(name = "createdBy", nullable = true)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "modifiedBy", nullable = true)
    private Long modifiedBy;

}