package com.wetube.comments.security;

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

private  String extractToken(HttpServletRequest request){
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

    public  Optional<String> extractUsername(HttpServletRequest request){
        try {
            String token =extractToken(request);
            if (token != null && !isTokenExpired(token)) {
                DecodedJWT decodedJWT = JWT.decode(token);
                return Optional.ofNullable(decodedJWT.getSubject());
            }
        }catch (Exception e){
            throw new RuntimeException("token invalido o usuario no autenticado");
        }
        return Optional.empty();
    }

    public String getUsernameOrThrow(HttpServletRequest request){
    return extractUsername(request)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuario no autenticado"));
    }

}
