package com.crud.card.gestionrh.api.dto.response;

import com.crud.card.gestionrh.domain.model.Role;
import lombok.Data;

@Data
public class RoleResponse {

    private Long id;
    private String name;
    private String description;

    public static RoleResponse fromEntity(Role role) {
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        return response;
    }
}
