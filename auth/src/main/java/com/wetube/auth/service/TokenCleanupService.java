package com.wetube.auth.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wetube.auth.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;

@Component
public class TokenCleanupService {

private final RefreshTokenRepository refreshTokenRepository;

    public TokenCleanupService(RefreshTokenRepository refreshTokenRepository){
    this.refreshTokenRepository=refreshTokenRepository;
}

@Transactional
@Scheduled(cron = "0 0 * * * *") //repetir el metodo cada hora en punto
    public void deleteExpiredTokens(){
    refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
}

}
