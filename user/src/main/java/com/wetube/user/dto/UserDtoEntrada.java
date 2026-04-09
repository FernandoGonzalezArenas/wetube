package com.wetube.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDtoEntrada {

    @Schema(description = "descripcion de el perfil de el usuario", example = "soy fernando_dev y este es mi perfil")
    @Size(max = 1000)
private String bio;

    @Schema(description = "nombre unico de el archivo de la foto de perfil", example = "5ghvhthr3gjgjh-perfil.jpg")
private String profilePictureUrl;

}
