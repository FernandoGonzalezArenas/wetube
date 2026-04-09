package com.wetube.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @Schema(description = "nombre unico de usuario", example = "fernando_dev")
    @NotBlank(message = "el nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "el nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @Schema(description = "contraseña segura", example = "P@ssw0rd2026")
    @NotBlank(message = "la contraseña es obligatoria")
    @Size(min = 8, max = 200, message = "la contraseña debe tener almenos 8 caracteres")
    private String password;

}
