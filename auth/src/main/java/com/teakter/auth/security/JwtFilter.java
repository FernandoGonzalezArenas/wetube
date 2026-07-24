package com.teakter.auth.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService){
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
    }

protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
throws ServletException, IOException {
String authorizationHeader=request.getHeader("Authorization");
if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
    String token=authorizationHeader.substring(7);
    String username= jwtUtil.extractUsername(token);

    if (username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);

        if (jwtUtil.isTokenValid(token, userDetails.getUsername())){
            UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
chain.doFilter(request, response);
}

}
