package com.example.demo.entity.chapter;

import com.example.demo.entity.supports.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_chapter_img")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Builder
public class ChapterImg {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Setter(AccessLevel.NONE)
    private String id;

    private String fileName;
    private String fileType;
    @Column(columnDefinition = "TEXT")
    private String fileUrl;
    @Column
    @CreatedBy
    private String createdBy;
    @Column
    @CreatedDate
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime createdDate;
    @Column
    @LastModifiedBy
    private String modifiedBy;
    @Column
    @LastModifiedDate
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    private int imgNumber;
}
