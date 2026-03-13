package com.wetube.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

    @NotBlank(message = "el nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "el nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotBlank(message = "la contraseña es obligattoria")
    @Size(min = 8, max = 200, message = "la contraseña debe tener almenos 8 caracteres")
    private String password;

    private String role;

    @NotBlank(message = "el correo es obligatorio")
@Email(message = "debe ser un correo electronico valido")
    private String email;

@Size(min = 10, max = 100, message = "la direccion debe tener almenos 10 caracteres")
    private String address;

@Pattern(regexp = "^[0-9]{10}$", message = "el numero de telefono debe tener 10 digitos")
    private String phone;

}
