package com.nexuscoreserver.beans.system;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SystemUsersRequestObject {

    @NotNull(message = "The username is required.")
    private String userName;

    @NotBlank(message = "The password is required.")
    private String password;

    /**
     * Campo opcional para el flujo de autenticación de doble factor.
     * Si is2faEnabled es true en el modelo, este campo será obligatorio en la validación del servicio.
     */
    private String totpCode;
}