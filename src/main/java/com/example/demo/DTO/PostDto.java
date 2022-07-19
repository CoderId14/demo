package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class PostDto {
    private Long postId;
    private String title;
    private String body;
    private String path;
    private Boolean deleted;
    private Long createdBy;
    private Long modifiedBy;
    private LocalDateTime created;
    private LocalDateTime modified;
}