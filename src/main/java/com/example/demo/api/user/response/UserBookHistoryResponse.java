package com.example.demo.api.user.response;


import com.example.demo.api.book.response.BookResponse;
import com.example.demo.api.chapter.response.ChapterResponse;
import com.example.demo.dto.AbstractDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserBookHistoryResponse extends AbstractDTO {
    private BookResponse book;
    private ChapterResponse recentlyChapter;
    private long userId;
    private String username;
}
