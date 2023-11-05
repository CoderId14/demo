package com.example.demo.api.role.response;

import com.example.demo.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RoleResponse extends AbstractDTO {
    private String roleName;
}
