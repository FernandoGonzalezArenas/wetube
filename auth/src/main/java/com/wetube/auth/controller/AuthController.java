package com.wetube.auth.controller;

import com.wetube.auth.dto.AuthResponse;
import com.wetube.auth.dto.LoginRequest;
import com.wetube.auth.dto.RefreshTokenRequest;
import com.wetube.auth.dto.RegisterRequest;
import com.wetube.auth.service.AuthService;
import com.wetube.auth.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 @RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService){
        this.authService=authService;
        this.refreshTokenService=refreshTokenService;
    }

@PostMapping("/register")
public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("usuario creado correctamente");
}

@PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response=authService.login(request);
        return ResponseEntity.ok(response);
}

@PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        AuthResponse response=refreshTokenService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
}

@PostMapping("/logout")
public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest request){
    refreshTokenService.deleteByToken(request.getRefreshToken());
    return ResponseEntity.ok("sesion cerrada correctamente");
    }
}
