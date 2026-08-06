package com.teakter.auth.repository;

import com.teakter.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying
    void deleteAllByExpiryDateBefore(Instant date);

    @Modifying
    void deleteByUserId(Long userId);
}
