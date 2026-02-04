package com.wetube.gateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

public DecodedJWT validateToken(String token){
    return JWT.require(Algorithm.HMAC256(secretKey))
            .build()
            .verify(token);
}

public String extractUserId(String token){
    return validateToken(token).getClaim("userId").asString();
}

public boolean isTokenExpired(String token){
    return validateToken(token).getExpiresAt().before(new Date());
}

}
