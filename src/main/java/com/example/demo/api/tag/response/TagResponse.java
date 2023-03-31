package com.example.demo.api.tag.response;


import com.example.demo.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TagResponse extends AbstractDTO {
    private String name;
    private String description;

}
