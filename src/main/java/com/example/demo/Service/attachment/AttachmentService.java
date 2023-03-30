package com.example.demo.Service.attachment;


import com.example.demo.Repository.AttachmentRepo;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.api.attachment.response.AttachmentResponse;
import com.example.demo.entity.Attachment;
import com.example.demo.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

@Service
public class AttachmentService implements IAttachmentService {
    private final AttachmentRepo attachmentRepository;

    public AttachmentService(AttachmentRepo attachmentRepository){
        this.attachmentRepository = attachmentRepository;
        try{
            Path storageFolder = FileSystems.getDefault().getPath("upload");
            Files.createDirectories(storageFolder);
        }catch (IOException e){
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }


    public AttachmentResponse saveAttachment(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(fileName.contains("..")){
                throw new RuntimeException("Filename contain invalid path sequence" + fileName);
            }
            Attachment attachment =
                    new Attachment(fileName,file.getContentType(),
                            file.getBytes());

            attachmentRepository.save(attachment);
            return AttachmentResponse.builder()
                    .id(attachment.getId())
                    .fileName(attachment.getFileName())
                    .fileType(attachment.getFileType())
                    .createdDate(attachment.getCreatedDate())
                    .modifiedDate(attachment.getModifiedDate())
                    .createdBy(attachment.getCreatedBy())
                    .modifiedBy(attachment.getModifiedBy())
                    .build();
        }
        catch (Exception e){
            throw new RuntimeException("Could not save file");
        }
    }

    public AttachmentResponse getAttachment(String fileId) {
        Attachment attachment = attachmentRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("file attachment",fileId,fileId)
        );
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .fileName(attachment.getFileName())
                .fileType(attachment.getFileType())
                .createdDate(attachment.getCreatedDate())
                .modifiedDate(attachment.getModifiedDate())
                .data(attachment.getData())
                .createdBy(attachment.getCreatedBy())
                .modifiedBy(attachment.getModifiedBy())
                .build();
    }

    public boolean isImageFile(MultipartFile file){
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList( new String[] {"png","jpg","jpeg","bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    public Attachment saveImg(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(isImageFile(file)){
                Attachment attachment =
                        new Attachment(fileName,file.getContentType(),
                                file.getBytes());
                return attachmentRepository.save(attachment);
            }

            throw new RuntimeException("Filename contain invalid path sequence" + fileName);

        }
        catch (Exception e){
            throw new RuntimeException("Could not save file");
        }
    }

    public byte[] readFileContent(String fileId){
        Attachment attachment = attachmentRepository.findById(fileId).orElseThrow(
                        () -> new ResourceNotFoundException("file attachment","fileId",fileId)
                );
        return attachment.getData();
    }

    public ApiResponse deleteFile(String fileId){
        Attachment data =
                attachmentRepository.findById(fileId).orElseThrow(
                        () -> new ResourceNotFoundException("file attachment","fileId",fileId)
                );
        attachmentRepository.delete(data);
        return new ApiResponse(true, "Delete successfully filename "+ fileId);
    }

    public AttachmentResponse updateFile(String fileId, MultipartFile file)  {
        Attachment attachment =
                attachmentRepository.findById(fileId).orElseThrow(
                        () -> new ResourceNotFoundException("file attachment",fileId,fileId)
                );
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            attachment.setFileName(fileName);
            attachment.setData(file.getBytes());
            attachment.setFileType(file.getContentType());
            attachmentRepository.save(attachment);
            return AttachmentResponse.builder()
                    .id(attachment.getId())
                    .fileName(attachment.getFileName())
                    .fileType(attachment.getFileType())
                    .createdDate(attachment.getCreatedDate())
                    .modifiedDate(attachment.getModifiedDate())
                    .createdBy(attachment.getCreatedBy())
                    .modifiedBy(attachment.getModifiedBy())
                    .build();
        }catch (IOException e){
            throw new RuntimeException("Error with file");
        }
    }
}
