package com.example.demo.Repository.book.writer.dto;

public interface BookWriterDTO {
    Long getId();

    Long getBookId();

    Long getUserId();

    String getFullName();

    String getTitle();

    String getDescription();

    String getCover();

    String getStatus();
}
