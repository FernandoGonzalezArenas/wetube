package com.wetube.user.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class GatewayHeaderFilter extends OncePerRequestFilter {

@Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
    String userId_str=request.getHeader("X-User-Id");
    if (userId_str!=null && !userId_str.isEmpty()){
try {
    Long userId = Long.parseLong(userId_str);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            userId,
            null,
            Collections.emptyList()
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
}catch (NumberFormatException e){
logger.error("formato de X-User-Id invalido: "+ userId_str);
}
    }
filterChain.doFilter(request, response);
}

}
