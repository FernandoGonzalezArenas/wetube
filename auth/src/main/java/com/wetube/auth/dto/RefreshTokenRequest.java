package com.wetube.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {

@NotBlank(message = "el refreshToken es obligatorio")
    private String refreshToken;
}
