package com.wetube.likes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilTest {

private JwtUtil jwtUtil;
private final String SECRET="testSecretKey123";

@BeforeEach
    void setup(){
    jwtUtil=new JwtUtil();
    ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET);
}

@Test
    void extractUserId_ShouldReturnId_WhenTokenIsValid(){
String token= JWT.create()
        .withClaim("userId", "50")
        .withExpiresAt(new Date(System.currentTimeMillis() + 100000))
        .sign(Algorithm.HMAC256(SECRET));

    HttpServletRequest request= mock(HttpServletRequest.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer "+ token);

    Optional<Long> userId=jwtUtil.extractUserId(request);

    //validaciones
    assertTrue(userId.isPresent());
    assertEquals(50L, userId.get());
}

@Test
    void extractUserId_ShouldReturnEmpty_WhenTokenIsMissing(){
    HttpServletRequest request=mock(HttpServletRequest.class);
    when(request.getHeader("Authorization")).thenReturn(null);

            Optional<Long> userId = jwtUtil.extractUserId(request);

            assertFalse(userId.isPresent());
        }

        @Test
    @DisplayName("debe extraer el userId de el token incluso si viene como numero en el JSON (con comillas)")
    void shouldExtractUserIdWhenItIsWrappedInQuotes(){
    //simular un token
            String token= JWT.create()
                    .withClaim("userId", "77")
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(SECRET));

            HttpServletRequest request=mock(HttpServletRequest.class);

            when(request.getHeader("Authorization")).thenReturn("Bearer "+token);
            Optional<Long> userId=jwtUtil.extractUserId(request);

            //validaciones
            assertTrue(userId.isPresent());
            assertEquals(77L, userId.get());
        }

}
