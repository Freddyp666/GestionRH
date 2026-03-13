package com.crud.card.gestionrh.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeResponse {

    private String mensaje;
    private boolean success;
    private Object data;


    //Constructor solo con un mensaje(De exito)
    public  MensajeResponse(String mensaje) {
        this.mensaje = mensaje;
        this.success = true;
    }

    //Constructor con errores
    public  MensajeResponse(String mensaje, boolean success) {
        this.mensaje = mensaje;
        this.success = success;
    }

    //Metodo estitc para respuestas exitosas
    public static MensajeResponse success(String mensaje) {
        return new MensajeResponse(mensaje, true);
    }

    //Metodo estico para respuesta exitoras con datos
    public static MensajeResponse success(String mensaje, Object data) {
        MensajeResponse mensajeResponse = new MensajeResponse(mensaje, true);
        mensajeResponse.setData(data);
        return mensajeResponse;
    }

    //Metodo estico para erroes
    public static MensajeResponse error(String mensaje) {
        return new MensajeResponse(mensaje, false);
    }
}
