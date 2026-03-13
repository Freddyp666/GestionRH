package com.crud.card.gestionrh.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message="El nombre de usuario es Obligatorio")
    private String username;

    @NotBlank(message="La contrasenia es obligatorio")
    private String password;

}
