package com.wetube.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportCreateDto {

    @Schema(description = "Motivo del reporte", example = "SPAM")
    private String reason; // Se recibe como String desde el select de tu Front

    @Schema(description = "Descripción detallada del reporte", example = "El usuario sube contenido repetido")
    private String description;
}