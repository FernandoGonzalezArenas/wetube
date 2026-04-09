package com.wetube.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoMetadataDto {

    @Schema(description = "titulo de el video reportado", example = "mi video")
private String title;

    @Schema(description = "descripcion de el video reportado", example = "esta es la descripcion de mi video")
private String description;

    @Schema(description = "URL de el video reportado para reproducirlo", example = "http://minio:9000/bucket/videos/hausyure84yeydfh-video.mp4")
private String videoUrl;

    @Schema(description = "URL de la miniatura de el video reportado", example = "http://minio:9000/buket/thumbnails/6sgfhee44dhjdhgdujhfd-thumb.jpg")
private String thumbnailUrl;

}
