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

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

@InjectMocks
    private JwtUtil jwtUtil;

@Mock
    private HttpServletRequest request;

private final String SECRET="test_secret_key_12345678901234567890";

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
    @DisplayName("debe extraer el userId de el token incluso si viene como numero en el JSON (con comillas)")
void shouldExtractUserIdWhenItIsWrappedInQuotes(){
    //simulamos un token donde el claim no es un string puro
    String token= JWT.create()
            .withClaim("userId", "77")
            .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
            .sign(Algorithm.HMAC256(SECRET));

    when(request.getHeader("Authorization")).thenReturn("Bearer "+ token);
Optional<Long> userId=jwtUtil.extractUserId(request);

//validaciones
    assertTrue(userId.isPresent());
    assertEquals(77L, userId.get());
}

}
