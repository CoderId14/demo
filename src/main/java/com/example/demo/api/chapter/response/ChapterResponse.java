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
public class ChapterResponse extends AbstractDTO {
    private String title;
    private int chapterNumber;
    private Long bookId;
}
