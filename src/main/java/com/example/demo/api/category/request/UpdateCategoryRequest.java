package com.example.demo.api.category.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateCategoryRequest {
    private Long id;
    @NotBlank
    private String categoryName;
    private String description;
}
