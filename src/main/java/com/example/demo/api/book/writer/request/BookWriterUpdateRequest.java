package com.example.demo.api.book.writer.request;

import com.example.demo.entity.book.WriterRequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterUpdateRequest {
    private Long bookId;
    private Long userId;
    private WriterRequestStatus status;
}
