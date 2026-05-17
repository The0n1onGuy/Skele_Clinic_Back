package com.nexuscoreserver.beans.system;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestObject {

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String userName;

    @NotBlank(message = "La contraseña es obligatoria.")
    private String password;

    /**
     * Campo opcional para el flujo de autenticación de doble factor.
     * Solo se enviará en el Endpoint B (Verificación 2FA).
     */
    private String totpCode;
}