package com.wetube.video.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

@InjectMocks
    private JwtUtil jwtUtil;

@Mock
    private HttpServletRequest request;

private final String SECRET="secret-test";

@BeforeEach
    void setup(){
    ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET);
}

@Test
@DisplayName("debe extraer userId de un token valido")
    void shouldExtractUserIdFromValidToken(){
String token= JWT.create()
        .withClaim("userId", "10")
        .withExpiresAt(new Date(System.currentTimeMillis()+ 60000))
        .sign(Algorithm.HMAC256(SECRET));

when(request.getHeader("Authorization"))
        .thenReturn("Bearer "+ token);

    Optional<Long> userId=jwtUtil.extractUserId(request);
assertTrue(userId.isPresent());
assertEquals(10L, userId.get());
}

@Test
    @DisplayName("debe retornar Empty si el token expiro")
    void shouldReturnEmptyIfTokenExpired(){
    String token=JWT.create()
            .withClaim("userId", "10")
            .withExpiresAt(new Date(System.currentTimeMillis() - 60000))
            .sign(Algorithm.HMAC256(SECRET));

    when(request.getHeader("Authorization")).thenReturn("Bearer "+token);
    Optional<Long> userId=jwtUtil.extractUserId(request);;
    assertTrue(userId.isEmpty());
}

@Test
    @DisplayName("debe lanzar excepcion si se requiere usuario y no hay token")
    void shouldThrowExceptionIfRequiredAndNoToken(){
    when(request.getHeader("Authorization")).thenReturn(null);
    assertThrows(ResponseStatusException.class, () -> {
        jwtUtil.getUseridOrThrow(request);
    });
}

}
