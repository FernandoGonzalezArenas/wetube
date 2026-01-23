package com.wetube.likes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public Optional<Long> extractUserId(HttpServletRequest request){
try {
    String token = extractToken(request);
    if (token != null && !isTokenExpired(token)) {
        DecodedJWT decodedJWT = JWT.decode(token);

        String userIdStr=decodedJWT.getClaim("userId").asString();
        if (userIdStr!=null) {
            return Optional.of(Long.parseLong(userIdStr));
        }
    }
}catch (Exception e){
return Optional.empty();
}
return Optional.empty();
    }

private String extractToken(HttpServletRequest request){
        String authorizationHeader=request.getHeader("Authorization");
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.substring(7);
        }
return null;
    }

    public DecodedJWT validateToken(String token){
        return JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
    }

    public boolean isTokenExpired(String token){
        return validateToken(token).getExpiresAt().before(new Date());
    }

    public Long getUseridOrThrow(HttpServletRequest request){
        return extractUserId(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuario no autenticado"));
    }

}
