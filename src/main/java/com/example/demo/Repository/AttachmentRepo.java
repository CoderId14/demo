package com.example.demo.Repository;

import com.example.demo.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepo extends JpaRepository<Attachment, String> {
}
