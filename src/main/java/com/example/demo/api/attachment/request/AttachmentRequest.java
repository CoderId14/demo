package com.example.demo.api.attachment.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentRequest {

    private String id;

    private String fileName;
    private String fileType;
    private byte[] data;

    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
}
