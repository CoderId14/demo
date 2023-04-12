package com.example.demo.api.chapter.response;

import com.example.demo.dto.AbstractDTO;
import com.example.demo.dto.PagedResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChapterImgResponse {
    private long chapterId;
    private PagedResponse<ImgChapter> imgChapterList;
}

