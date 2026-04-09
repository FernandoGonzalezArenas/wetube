package com.wetube.video.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDtoEntrada {

    @Schema(description = "titulo de el video", example = "mi video")
    @NotBlank(message = "el video debe tener un titulo")
    @Size(min = 1, max = 100)
    private String title;


    @Schema(description = "descripcion que el dueño de el video quiera ponerle", example = "este es mi video de prueba")
    @NotBlank(message = "escribe una descripcion de el video")
    @Size(min = 1, max = 1000)
    private String description;

    @Schema(description = "nombre unico de el archivo de el video", example = "65wjekthejhtui4tjke-video.mp4")
    private String filename;

    @Schema(description = "nombre unico de el archivo de la miniatura", example = "4gdnvkjdnjf6-thumb.jpg")
    private String thumbnailUrl;
}
