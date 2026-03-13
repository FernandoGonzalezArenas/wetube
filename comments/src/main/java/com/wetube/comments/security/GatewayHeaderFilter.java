package com.wetube.comments.security;

import com.wetube.comments.dto.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GatewayHeaderFilter extends OncePerRequestFilter {

@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
String userId_str=request.getHeader("X-User-Id");
    String username=request.getHeader("X-User-Name");
String role_str=request.getHeader("X-User-Role");
if (userId_str!=null && !userId_str.isEmpty() && username!=null && !username.isEmpty()) {
try {
Long userId=Long.parseLong(userId_str);
    UserPrincipal principal=new UserPrincipal(userId, username);
    List<SimpleGrantedAuthority> authorities=Collections.emptyList();
    if (role_str!=null && !role_str.isEmpty()){
        System.out.println("role pasado por el hasRole: "+role_str);
        String finalRole=role_str.startsWith("ROLE_") ? role_str : "ROLE_"+role_str;
        System.out.println("role despues de tratarlo para que tenga el formato correcto: "+finalRole);

        authorities=List.of(new SimpleGrantedAuthority(finalRole));
    }
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            principal,
            null,
authorities
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
}catch (NumberFormatException e){
logger.error("error con el formato de el X-User-Id: "+userId_str);
}
}
            filterChain.doFilter(request, response);
}


}
