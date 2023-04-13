package com.example.demo.api.chapter.response;

import com.example.demo.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class UpdateChapterImgResponse extends AbstractDTO {
    private String fileUrl;
}
