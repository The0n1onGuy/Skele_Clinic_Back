package com.nexuscore.beans.system;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SystemUsersRequestObject {

    @NotNull(message = "Se requiere el nombre de usuario.")
    private String userName;

    @NotBlank(message = "Se requiere una contraseña.")
    private String userPassword;

}
