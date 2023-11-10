package com.example.demo.api.attachment;

import com.example.demo.Service.attachment.AttachmentService;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.api.attachment.response.AttachmentResponse;
import com.example.demo.dto.response.ObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/attachment")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/v1/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file){
        String response =  attachmentService.uploadFile(file, null);

        return ResponseEntity.ok().body(ObjectResponse.builder()
                .status(HttpStatus.OK)
                .message("success")
                .responseData(response)
                .build());
    }
    @GetMapping("/v1/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId){
        AttachmentResponse response = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(response.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment: filename=\"" + response.getFileName() +"\"")
                .body(new ByteArrayResource(response.getData()));
    }

    @GetMapping("/v1/{fileId}")
    public ResponseEntity<byte[]> getImage(
        @PathVariable(name = "fileId") String fileId
    ){
        byte[] data = attachmentService.readFileContent(fileId);
        return ResponseEntity.ok().
        contentType(MediaType.IMAGE_JPEG).
        body(data);
    }

    @PutMapping("/v1/{fileId}")
    public ResponseEntity<?> updateFile(
            @PathVariable(name ="fileId") String fileId,
            @RequestParam("file") MultipartFile file
            ){

        AttachmentResponse response = attachmentService.updateFile(fileId,file);
        String downloadURL ="";
        downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/attachment/download/")
                .path(fileId)
                .toUriString();

        return ResponseEntity.created(URI.create(downloadURL)).body(
                response
        );
    }

    @DeleteMapping("/v1/{fileId}")
    public ResponseEntity<ApiResponse> deleteFile(
            @PathVariable(name = "fileId") String fileId
    ){
        String deletedURL ="";
        deletedURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/attachment/download/")
                .path(fileId)
                .toUriString();
        ApiResponse response = attachmentService.deleteFile(fileId);
        return ResponseEntity.created(URI.create(deletedURL)).body(response);
    }
}
