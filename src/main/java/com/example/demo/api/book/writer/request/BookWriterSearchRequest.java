package com.example.demo.api.book.writer.request;


import com.example.demo.Service.book.writer.dto.request.BookWriterSearchInput;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookWriterSearchRequest {
    private Long id;
    private Long bookId;
    private Long userId;
    private String status;

    private Integer page;
    private Integer size;
    private String sortBy;

    public BookWriterSearchInput toInput() {
        return BookWriterSearchInput.builder()
                .id(this.id)
                .bookId(this.bookId)
                .userId(this.userId)
                .status(this.status)
                .build();
    }
}
