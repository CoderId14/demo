package com.example.demo.api.chapter.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateBulkChapterImgRequest {
    private long chapterId;
    private List<ChapterImgDto> listImg;
}
