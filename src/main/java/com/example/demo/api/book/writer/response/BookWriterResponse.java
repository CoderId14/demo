package com.example.demo.api.book.writer.response;


import com.example.demo.Repository.book.writer.dto.BookWriterDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterResponse {

    private Long id;

    private Long bookId;
    private Long userId;
    private String username;
    private String title;

    private String description;

    private String cover;

    private String status;

    public static BookWriterResponse from(BookWriterDTO input){
        return BookWriterResponse.builder()
                .id(input.getId())
                .bookId(input.getBookId())
                .userId(input.getUserId())
                .username(input.getUsername())
                .title(input.getTitle())
                .description(input.getDescription())
                .cover(input.getCover())
                .status(input.getStatus())
                .build();
    }
}
