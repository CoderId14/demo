package com.example.demo.api.chapter.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImgChapter {
    private String id;
    private String fileUrl;
    private int imgNumber;
}
