package com.example.demo.api.user.request;


import com.example.demo.Utils.AppConstants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateUserBookHistoryRequest {
    @NotNull(message = "User ID must not null")
    private Long userId;
    @NotNull(message = "Book ID must not null")
    private Long bookId;
    @NotNull(message = "Chapter ID must not null")
    private Long chapterId;
    @Builder.Default
    private int page = Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER);
    @Builder.Default
    private int size = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
}
