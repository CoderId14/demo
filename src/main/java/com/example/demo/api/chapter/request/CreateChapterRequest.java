package com.example.demo.api.chapter.request;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateChapterRequest {
    @NotNull
    private Long bookId;
    private String title;
    private String content;
    private String description;
}
