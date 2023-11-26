package com.example.demo.api.book.writer.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriterPromoteCreateRequest {
    private Long userId;
    private Long roleId;
    private String roleName;
    private String status;
}
