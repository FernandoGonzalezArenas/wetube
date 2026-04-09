package com.wetube.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditorDto {


    @Schema(description = "motivo de el reporte de el recurso", example = "violencia")
private String reason;


}
