package com.teakter.auth.repository;

import com.teakter.auth.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
    Optional<VerificationTokenEntity> findByToken(String token);

@Modifying
void  deleteByUserId(Long userId);

@Modifying
    void deleteAllByExpiryDateBefore(LocalDateTime now);

}
