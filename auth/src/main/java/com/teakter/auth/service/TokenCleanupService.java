package com.teakter.auth.service;

import com.teakter.auth.entity.UserEntity;
import com.teakter.auth.repository.RefreshTokenRepository;
import com.teakter.auth.repository.UserRepository;
import com.teakter.auth.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenCleanupService {

private final RefreshTokenRepository refreshTokenRepository;
private final UserRepository userRepository;
private final VerificationTokenRepository verificationTokenRepository;

@Transactional
@Scheduled(cron = "0 0 * * * *") //repetir el metodo cada hora en punto
    public void deleteExpiredTokens(){
//borra tokens de refresco expirados
    refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());

    //borra tokens de verificacion de correo expirados
    verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
}

@Transactional
    @Scheduled(cron = "0 0 2 * * ?")
    public void purgeUnverifiedUsers(){
    LocalDateTime limit=LocalDateTime.now().minusHours(24);

//buscar los usuarios que tengan tokens expirados hace mas de 24 horas
    List<UserEntity> unverifiedUsers=userRepository.findByIsVerifiedFalseAndCreatedAtBefore(limit);

if (!unverifiedUsers.isEmpty()){
    userRepository.deleteAll(unverifiedUsers);
}
}

}
