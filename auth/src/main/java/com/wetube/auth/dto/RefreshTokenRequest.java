package com.wetube.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {

    @Schema(description = "token de refresco", example = "eikwhuirh38shfjgrru...")
@NotBlank(message = "el refreshToken es obligatorio")
    private String refreshToken;
}
