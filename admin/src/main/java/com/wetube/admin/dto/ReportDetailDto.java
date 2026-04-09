package com.wetube.admin.dto;

import com.wetube.admin.entity.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailDto {

    @Schema(description = "ID de el reporte", example = "12")
private Long reportId; //id de el reporte

    @Schema(description = "tipo de reporte", example = "VIDEO", allowableValues = {"VIDEO", "USER"})
private ReportType type;

    @Schema(description = "ID de el recurso reportado (video o usuario)", example = "23")
    private Long targetId;

    @Schema(description = "motivo por el que se reporta el recurso", example = "violencia")
    private String reason;

    @Schema(description = "titulo de el video si el reportado es un video", example = "mi video")
    private String videoTitle;

    @Schema(description = "descripcion de el video reportado", example = "esta es la descripcion de el video reportado")
private String videoDescription;

    @Schema(description = "URL de reproduccion de el video", example = "http://minio:9000/bucket/videos/ashue-video.mp4")
private String videoUrl;

    @Schema(description = "URL de la miniatura de el video reportado", example = "http://minio:9000/bucket/thumbnails/ahduiyehjshfuue-thumb.jpg")
private String thumbnailUrl;


    @Schema(description = "nombre de usuario si la reportada es una cuenta de usuario", example = "fernando_dev")
private String targetUsername;

    @Schema(description = "estatus de el reporte", example = "PENDING", allowableValues = {"PENDING", "DISMISS", "RESOLVED"})
private String status;

    @Schema(description = "fecha de creacion de el reporte", example = "2024-05-27T11:14:07")
private LocalDateTime createdAt;

}
