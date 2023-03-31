package com.example.demo.api.category.response;

import com.example.demo.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CategoryResponse extends AbstractDTO {
    @NotBlank(message = "Category code must not blank" )
    private String name;
    private String description;
}
