package com.wetube.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Schema(description = "nombre unico de usuario", example = "fernando_dev")
    private String username;

    @Schema(description = "descripcion de el usuario en su perfil", example = "soy fernando_dev y este es mi perfil")
    private String bio;

    @Schema(description = "URL de la foto de perfil", example = "http://minio:9000/bucket/profiles/6ahfjkhjs7-perfil.jpg")
    private String profilePictureUrl;

    @Schema(description = "fecha de creacion de el perfil", example = "2026-05-18T08:36:57")
    private LocalDateTime createdAt;

}
