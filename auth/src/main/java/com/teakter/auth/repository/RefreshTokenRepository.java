package com.teakter.auth.repository;

import com.teakter.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
void deleteAllByExpiryDateBefore(Instant date);
void deleteByUserId(Long userId);
}
