package com.crud.card.gestionrh.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type="Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    //Constructor de los campos
    public JwtResponse(String token, String type, Long id, List<String> roles, String username, String email) {
        this.token = token;
        this.type = type;
        this.id=id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
