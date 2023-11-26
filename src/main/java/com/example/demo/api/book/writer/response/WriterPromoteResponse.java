package com.example.demo.api.book.writer.response;


import com.example.demo.Service.book.writer.dto.response.WriterPromoteSearchResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WriterPromoteResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private Long roleId;
    private String roleName;
    private String status;

    public static WriterPromoteResponse from(WriterPromoteSearchResponse input){
        return WriterPromoteResponse.builder()
                .id(input.getId())
                .userId(input.getUserId())
                .fullName(input.getFullName())
                .roleId(input.getRoleId())
                .roleName(input.getRoleName())
                .status(input.getStatus())
                .build();
    }
}
