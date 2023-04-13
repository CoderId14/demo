package com.example.demo.api.chapter.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateChapterImgRequest {
    private String fileUrl;
}
