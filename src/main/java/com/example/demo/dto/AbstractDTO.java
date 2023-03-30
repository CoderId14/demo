package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AbstractDTO {
    public long id;
    public String createdBy;
    public LocalDateTime createdDate;
    public String modifiedBy;
    public LocalDateTime modifiedDate;
}
