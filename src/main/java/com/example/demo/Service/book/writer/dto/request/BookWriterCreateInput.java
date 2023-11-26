package com.example.demo.Service.book.writer.dto.request;

import com.example.demo.entity.book.WriterRequestStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterCreateInput {
        private Long bookId;
        private Long userId;
        private WriterRequestStatus status;
}
