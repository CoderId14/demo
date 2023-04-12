package com.example.demo.api.chapter.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateChapterImgRequest {
    @NotNull
    private long chapterId;
    private MultipartFile chapterImg;
}
