package com.example.demo.Service.book.writer.dto.response;

import com.example.demo.Repository.user.writer.dto.UserPromoteDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WriterPromoteSearchResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private Long roleId;
    private String roleName;
    private String status;

    public static WriterPromoteSearchResponse from(UserPromoteDTO input){
        return WriterPromoteSearchResponse.builder()
                .id(input.getId())
                .userId(input.getUserId())
                .fullName(input.getFullName())
                .roleId(input.getRoleId())
                .roleName(input.getRoleName())
                .status(input.getStatus())
                .build();
    }
}
