package com.example.demo.api.category.response;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private long categoryId;
    private String categoryName;
    private String description;
    private LocalDateTime modifiedDate;
}
