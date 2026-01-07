package com.wetube.auth.service;

import java.util.Optional;

import com.wetube.auth.dto.AuthResponse;
import com.wetube.auth.entity.RefreshTokenEntity;

public interface RefreshTokenService {
Optional<RefreshTokenEntity> findByToken(String token);

AuthResponse generateTokensForUser(String username);

void registerRefresh(String refresh, String username);

 AuthResponse refreshToken(String refreshToken);

void deleteByToken(String token);
}
