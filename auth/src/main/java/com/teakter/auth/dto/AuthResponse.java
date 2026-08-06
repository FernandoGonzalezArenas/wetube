package com.teakter.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    @Schema(description = "token de acceso", example = "eisjliheoi39834hehedghrfjkgdfjf...")
    private String accessToken;

    @Schema(description = "token de refresco", example = "eijskfnwihjknfh8484...")
    private String refreshToken;

}
