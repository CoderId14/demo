package com.example.demo.api.chapter.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterImgDto {
    private String id;
    private String fileUrl;
}
