package com.example.demo.api.book.request;

import com.example.demo.api.book.request.validator.ValidThumbnail;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@ValidThumbnail
public class CreateBookRequest {
    private MultipartFile thumbnail;
    private String thumbnailUrl;
    @NotNull
    private String title;
    private String shortDescription;
    private String content;
    private Set<Long> categories;
    private Set<Long> tags;
    @Builder.Default
    @NotNull
    private Boolean isPremium = false;
    @Builder.Default
    @NotNull
    private Boolean isNovel = false;
}
