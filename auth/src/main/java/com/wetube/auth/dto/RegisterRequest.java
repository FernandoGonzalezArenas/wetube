package com.wetube.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

    @Schema(description = "nombre unico de usuario", example = "fernando_dev")
    @NotBlank(message = "el nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "el nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @Schema(description = "contraseña segura", example = "P@ssw0rd2026")
    @NotBlank(message = "la contraseña es obligattoria")
    @Size(min = 8, max = 200, message = "la contraseña debe tener almenos 8 caracteres")
    private String password;

    @Schema(description = "correo electronico", example = "contacto@wetube.com")
    @NotBlank(message = "el correo es obligatorio")
@Email(message = "debe ser un correo electronico valido")
    private String email;

    @Schema(description = "direccion fisica", example = "los sauses 862, col. la puerta de oro")
@Size(min = 10, max = 100, message = "la direccion debe tener almenos 10 caracteres")
    private String address;

    @Schema(description = "numero de telefono", example = "456-259-6138")
@Pattern(regexp = "^[0-9]{10}$", message = "el numero de telefono debe tener 10 digitos")
    private String phone;

}
