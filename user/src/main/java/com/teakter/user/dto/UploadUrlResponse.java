package com.teakter.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadUrlResponse(
        @Schema(description = "URL firmada de subida de el archivo", example = "http://minio:9000/bucket/file/jheughjkehurhjkkrgrui-file.ext?firma=firma")
        String uploadUrl,

        @Schema(description = "nombre final de el archivo para guardar en la base de datos", example = "76djlkjihjkfngu558fhk-archivo.ext")
        String filename) {
}
