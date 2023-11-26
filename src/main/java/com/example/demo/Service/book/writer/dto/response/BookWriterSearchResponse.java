package com.example.demo.Service.book.writer.dto.response;

import com.example.demo.Repository.book.writer.dto.BookWriterDTO;
import com.example.demo.api.book.writer.response.BookWriterResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterSearchResponse {
        private Long id;
        private Long bookId;
        private Long userId;
        private String bookName;
        private String description;
        private String fullName;
        private String status;
        private String cover;

        public static BookWriterSearchResponse from(BookWriterDTO input){
                return BookWriterSearchResponse.builder()
                        .id(input.getId())
                        .bookId(input.getBookId())
                        .userId(input.getUserId())
                        .fullName(input.getFullName())
                        .bookName(input.getTitle())
                        .description(input.getDescription())
                        .cover(input.getCover())
                        .status(input.getStatus())
                        .build();
        }
}
