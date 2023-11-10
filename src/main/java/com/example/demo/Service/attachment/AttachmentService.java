package com.example.demo.Service.attachment;


import com.example.demo.Repository.AttachmentRepo;
import com.example.demo.api.attachment.response.AttachmentResponse;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Attachment;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachmentService implements IAttachmentService {
    private final AttachmentRepo attachmentRepository;

    public AttachmentService(AttachmentRepo attachmentRepository){
        this.attachmentRepository = attachmentRepository;
    }
    public String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }

    public String uploadFile(MultipartFile multipartFile, String path){
        String objectName = generateFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        StorageOptions storageOptions = null;
        String bucketName = "spring-boot-demo-357108.appspot.com";
        try {
            storageOptions = StorageOptions.newBuilder()
                    .setProjectId("spring-boot-demo-357108")
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource("firebase_admin.json").getInputStream()))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Storage storage = storageOptions.getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(multipartFile.getContentType())
                .build();

        try {
            Blob blob = storage.create(blobInfo, multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("UPLOAD FILE" + multipartFile.getName() + " " + multipartFile.getSize() + " octet");
        return getPublicUrl(bucketName, objectName);
    }
    public String getPublicUrl(String bucketName, String objectName) {
        return "https://storage.cloud.google.com/" +bucketName + "/" + objectName;
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
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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
            throw new BadRequestException("Could not save file");
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
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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
