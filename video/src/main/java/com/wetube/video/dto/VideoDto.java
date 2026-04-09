package com.wetube.video.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {

@Schema(description = "titulo de el video", example = "mi video")
    private String title;

    @Schema(description = "descripcion que el dueño de el video quiera agregar", example = "este es mi video de prueba")
    private String description;

    @Schema(description = "nombre unico de el archivo de el video", example = "j3k45fsfjeuihjdgdk-video.mp4")
private String videoUrl;

    @Schema(description = "nombre unico de el archivo de la miniatura", example = "shsihhj335hjhuhd-thumb.jpg")
    private String thumbnailUrl;
}
