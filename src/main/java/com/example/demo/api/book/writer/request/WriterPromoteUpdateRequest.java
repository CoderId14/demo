package com.example.demo.api.book.writer.request;

import com.example.demo.entity.book.WriterRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriterPromoteUpdateRequest {
    private Long id;
    private Long userId;
    private Long roleId;
    private WriterRequestStatus status;
}
