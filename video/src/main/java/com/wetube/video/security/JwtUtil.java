package com.wetube.video.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    
    private static final Logger logger= LoggerFactory.getLogger(JwtUtil.class);

    public Optional<Long> extractUserId(HttpServletRequest request){
        try {
            String token=extractToken(request);
if (token==null) return Optional.empty();
    DecodedJWT decodedJWT= JWT.require(Algorithm.HMAC256(secretKey))
            .build()
            .verify(token);
    String userID_str=decodedJWT.getClaim("userId").asString();
    if (userID_str==null){
userID_str=decodedJWT.getClaim("userId").toString().replace("\"", "");
    }

    if (userID_str==null || userID_str.isEmpty() ||userID_str.equals("null")){
        logger.warn("el token tiene un formato valido pero el userId esta vacío");
        return Optional.empty();
    }
    return Optional.of(Long.parseLong(userID_str));
        }catch (Exception e){
            logger.error("error con la extraccion de el userId {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static String extractToken(HttpServletRequest request){
        String authorizationHeader=request.getHeader("Authorization");
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            return authorizationHeader.substring(7);
        }
return null;
    }

}
