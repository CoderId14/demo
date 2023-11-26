package com.example.demo.api.book.writer.response;


import com.example.demo.Service.book.writer.dto.response.BookWriterSearchResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterResponse {

    private Long id;

    private Long bookId;
    private Long userId;
    private String fullName;
    private String title;

    private String description;

    private String cover;

    private String status;

    public static BookWriterResponse from(BookWriterSearchResponse input){
        return BookWriterResponse.builder()
                .id(input.getId())
                .bookId(input.getBookId())
                .userId(input.getUserId())
                .fullName(input.getFullName())
                .title(input.getBookName())
                .description(input.getDescription())
                .cover(input.getCover())
                .status(input.getStatus())
                .build();
    }
}
