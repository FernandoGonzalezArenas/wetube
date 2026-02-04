package com.wetube.comments.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public  Optional<String> extractUsername(HttpServletRequest request){
        try {
            String token =extractToken(request);
if (token==null) return Optional.empty();

                DecodedJWT decodedJWT =JWT.require(Algorithm.HMAC256(secretKey))
                        .build()
                        .verify(token);
                return Optional.ofNullable(decodedJWT.getSubject());
        }catch (Exception e){
return Optional.empty();
        }
    }

}
