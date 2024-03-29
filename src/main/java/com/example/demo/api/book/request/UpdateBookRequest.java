package com.example.demo.api.book.request;

import com.example.demo.api.book.request.validator.ValidThumbnail;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@ValidThumbnail
@Builder
public class UpdateBookRequest {
    @NotNull
    private String title;
    private String content;
    private String thumbnailUrl;
    private  String shortDescription;

    private Set<Long> categories;

    private Set<Long> tags;

    private Long author;
    @Builder.Default
    @NotNull
    private Boolean isPremium = false;

    @Builder.Default
    @NotNull
    private Boolean isNovel = true;

    @Builder.Default
    private Boolean isActive = true;
}
