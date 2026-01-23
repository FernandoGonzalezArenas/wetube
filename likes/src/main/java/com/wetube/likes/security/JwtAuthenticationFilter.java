package com.wetube.likes.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil){
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
if (SecurityContextHolder.getContext().getAuthentication()==null){
        jwtUtil.extractUserId(request).ifPresent(userId -> {
    UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(
            userId,
            null,
            Collections.emptyList()
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
});
        }
filterChain.doFilter(request, response);
    }

}
