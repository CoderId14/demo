package com.example.demo.api.user.response;


import com.example.demo.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserBookHistoryResponse extends AbstractDTO {
    private Long userId;
    private String username;
    private Long bookId;
    private String bookTitle;
    private Long chapterId;
    private String chapterTitle;

    private int chapterNumber;
}
