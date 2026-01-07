package com.wetube.video.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    
    private static final Logger logger= LoggerFactory.getLogger(JwtUtil.class);

    public Optional<Long> extractUserId(HttpServletRequest request){
        try {
            String token=extractToken(request);
if (token!=null && !isTokenExpired(token)){
    DecodedJWT decodedJWT= validateToken(token);
    String userID_str=decodedJWT.getClaim("userId").asString();
    if (userID_str==null){
        logger.warn("el token no contiene el userId");
        return Optional.empty();
    }
return Optional.of(Long.parseLong(userID_str));
}
        }catch (Exception e){
            logger.error("error con la extraccion de el userId", e.getMessage(), e);
            return Optional.empty();
        }
return Optional.empty();
    }

    private static String extractToken(HttpServletRequest request){
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
