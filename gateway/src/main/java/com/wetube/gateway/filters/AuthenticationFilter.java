package com.wetube.gateway.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wetube.gateway.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

private final JwtUtil jwtUtil;

@Autowired
    public AuthenticationFilter(JwtUtil jwtUtil){
    this.jwtUtil=jwtUtil;
}

@Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
    ServerHttpRequest request= exchange.getRequest();
String path=request.getURI().getPath();
String method=request.getMethod().name();

//rutas permitidas sin token
    if (path.startsWith("/auth/register") || path.startsWith("/auth/login") || path.startsWith("/auth/refresh") || path.startsWith("/actuator")){
        return chain.filter(exchange);
    }

    //definicion de rutas publicas GET
    boolean isPublicGet=method.equals("GET") && (path.matches("/videos/interactions/\\d+") ||
        path.matches("/comentarios/\\d+") ||
        path.matches("/like/\\d+/count") ||
        path.matches("/like/\\d+/status") ||
        path.equals("/videos/feed") ||
        path.equals("/videos/search"));

//obtener el hencabezado con el token
    String authHeader= request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
if (authHeader == null || !authHeader.startsWith("Bearer ")){
if (isPublicGet){
    return chain.filter(exchange);
}
return unauthorizedResponse(exchange, "no hay token para el recurso protegido");
}

String token= authHeader.substring(7);
try {
    //validar token
    if (jwtUtil.isTokenExpired(token)){
        return unauthorizedResponse(exchange, "expired token");
    }
String userId=jwtUtil.extractUserId(token);

    //mutar la peticion para que el token siga y agregar el userId limpio
    ServerWebExchange mutatedExchange=exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header("X-User-Id", userId)
            .build())
        .build();

return chain.filter(mutatedExchange);
}catch (JWTVerificationException e){
return unauthorizedResponse(exchange, "Invalid token");
}
}

private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message){
    ServerHttpResponse response= exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
String json= String.format("{\"error\": \"%s\"}", message);

    byte[] bytes=json.getBytes(StandardCharsets.UTF_8);
    DataBuffer buffer=response.bufferFactory().wrap(bytes);
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

    return response.writeWith(Mono.just(buffer));
}

@Override
    public int getOrder(){
    return -1;
}

}
