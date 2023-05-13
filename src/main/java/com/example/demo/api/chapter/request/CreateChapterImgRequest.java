package com.example.demo.api.chapter.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateChapterImgRequest {
    private String id;
    @NotNull
    private long chapterId;
    private String fileUrl;
}
