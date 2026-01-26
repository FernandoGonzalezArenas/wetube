package com.wetube.comments.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
public class JwtUtilTest {

private JwtUtil jwtUtil;
private final String SECRET="test_secret_key_12345678901234567890";

@BeforeEach
    void setup(){
    jwtUtil=new JwtUtil();
    ReflectionTestUtils.setField(jwtUtil, "secretKey", SECRET);
}

@Test
    void extractUsername_ShouldReturnUsername_WhenTokenIsValid(){
    String token= JWT.create()
            .withSubject("fernando")
            .withExpiresAt(new Date(System.currentTimeMillis() + 100000))
            .sign(Algorithm.HMAC256(SECRET));

    HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer "+token);
    Optional<String> username=jwtUtil.extractUsername(request);

    //validaciones
    assertTrue(username.isPresent());
    assertEquals("fernando", username.get());
}

@Test
    void extractUsername_ShouldReturnEmpty_WhenTokenIsExpired(){
    //token expirado hace una hora
    String token= JWT.create()
            .withSubject("user")
            .withExpiresAt(new Date(System.currentTimeMillis()- 3600000))
            .sign(Algorithm.HMAC256(SECRET));
    HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
Optional<String> username=jwtUtil.extractUsername(request);

//validacion
assertTrue(username.isEmpty());
}

}
