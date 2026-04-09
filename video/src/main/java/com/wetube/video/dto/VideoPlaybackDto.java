package com.wetube.video.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoPlaybackDto {


    @Schema(description = "identificador de el video seleccionado", example = "26")
private Long id;

    @Schema(description = "titulo de el video seleccionado", example = "mi video")
private String title;

    @Schema(description = "descripcion de el video seleccionado", example = "este es mi video de prueba")
private String description;

    @Schema(description = "URL de reproduccion de el video seleccionado", example = "http://minio:9000/bucket/videos/7sghrukhdj-video.mp4?firma=firma")
private String videoUrl;

    @Schema(description = "URL de la miniatura", example = "http://minio:9000/bucket/thumbnails/s7ghhghjd5-thumb.jpg")
private String thumbnailUrl;

}
