package com.example.demo.Service.book.writer.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterRequestInput {

        private Long id;
        private Long bookId;
        private Long userId;
        private String status;
}
