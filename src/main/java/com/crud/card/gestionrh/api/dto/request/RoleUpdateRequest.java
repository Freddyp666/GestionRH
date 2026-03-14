package com.crud.card.gestionrh.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUpdateRequest {

    @NotNull(message = "EL ID DEL USUARIO ES OBLIGATORIO")
    private long usuarioId;

    @NotNull(message = "El ID DEL NUEVO ROL ES OBLIGATORIO")
    private long nuevorolId;
}
