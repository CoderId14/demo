package com.example.demo.api.attachment.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AttachmentResponse {
    private String id;
    private String fileName;
    private String fileType;
    private byte[] data;
    private String createdBy;
    private LocalDateTime createdDate;
    private String modifiedBy;
    private LocalDateTime modifiedDate;

}
