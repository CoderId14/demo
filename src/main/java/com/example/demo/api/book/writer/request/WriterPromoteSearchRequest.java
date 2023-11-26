package com.example.demo.api.book.writer.request;

import com.example.demo.Service.book.writer.dto.request.WriterPromoteSearchInput;
import com.example.demo.entity.book.WriterRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriterPromoteSearchRequest {
    private Long id;
    private Long userId;
    private Long roleId;
    @Builder.Default
    private WriterRequestStatus status = WriterRequestStatus.PENDING;

    private Integer page;
    private Integer size;
    private String sortBy;

    public WriterPromoteSearchInput toInput() {
        return WriterPromoteSearchInput.builder()
                .id(this.id)
                .userId(this.userId)
                .roleId(this.roleId)
                .status(this.status.getStatus())
                .build();
    }

}
