package com.crud.card.gestionrh.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min=3, max=100, message = "El nombre de usuario deber tener 3 y 100 palabra")
    private String username;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo válido")
    private String email;


    @NotBlank(message = "La contrsenia es obligatorio")
    @Size(min=6, message = "La contrasenia debe tener al menos 6 caracteres")
    private String password;

}
