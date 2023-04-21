package com.example.demo.api.chapter.response;

import com.example.demo.dto.AbstractDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class CreateChapterImgResponse extends AbstractDTO {
    private long chapterId;
    private String fileUrl;
}
