package com.example.demo.api.user.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeBookRequest {
    @NotNull(message = "bookId must not empty")
    private Long bookId;
}
