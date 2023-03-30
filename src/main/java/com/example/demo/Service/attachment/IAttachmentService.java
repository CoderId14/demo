package com.example.demo.Service.attachment;


import com.example.demo.api.attachment.response.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAttachmentService {

    AttachmentResponse saveAttachment(MultipartFile file);

    AttachmentResponse getAttachment(String fileId);
}
