package com.example.demo.api.user.request;


import com.example.demo.Utils.AppConstants;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserBookHistoryRequest {
    @NotNull
    private Long userId;
    @Builder.Default
    private int page = AppConstants.DEFAULT_PAGE_NUMBER;
    @Builder.Default
    private int size = AppConstants.DEFAULT_PAGE_SIZE;
}
