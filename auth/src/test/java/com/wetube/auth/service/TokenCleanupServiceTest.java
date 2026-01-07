package com.wetube.auth.service;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wetube.auth.repository.RefreshTokenRepository;

public class TokenCleanupServiceTest {

@Test
void  deleteExpiredTokens_invocaRepoConAhora(){
    RefreshTokenRepository repo=mock(RefreshTokenRepository.class);
    TokenCleanupService svc=new TokenCleanupService(repo);

    svc.deleteExpiredTokens();
    verify(repo, times(1)).deleteAllByExpiryDateBefore(any(Instant.class));
}

}
