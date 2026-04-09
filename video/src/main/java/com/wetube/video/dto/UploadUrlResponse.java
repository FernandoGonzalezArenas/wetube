package com.wetube.video.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadUrlResponse(
        @Schema(description = "URL firmada de subida de archivos a el servicio de almacenamiento deseado", example = "http://minio:9000/bucket/file/6sdgjwsw4sjshfje-file.ext?firma=firma")
        String uploadUrl,

        @Schema(description = "nombre final de el archivo", example = "5ashjkhfuewje7sghjdsh-file.ext")
        String finalFileName) {
}
