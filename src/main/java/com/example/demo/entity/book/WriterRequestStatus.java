package com.example.demo.entity.book;

import lombok.Getter;

@Getter
public enum WriterRequestStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED");

    private String status;

    WriterRequestStatus(String status) {
        this.status = status;
    }
}
