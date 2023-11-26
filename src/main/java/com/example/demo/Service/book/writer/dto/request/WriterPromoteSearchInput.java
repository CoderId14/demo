package com.example.demo.Service.book.writer.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WriterPromoteSearchInput {
    private Long id;
    private Long userId;
    private Long roleId;
    private String status;

}
