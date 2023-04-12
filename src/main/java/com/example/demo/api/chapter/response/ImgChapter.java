package com.example.demo.api.chapter.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImgChapter {
    private String imgId;
    private int imgNumber;
}
