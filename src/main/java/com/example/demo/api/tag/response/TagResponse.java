package com.example.demo.api.tag.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse{
    private long tagId;
    private String tagName;
    private String description;
    private LocalDateTime modifiedDate;
}
