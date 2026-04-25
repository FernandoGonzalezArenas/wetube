package com.wetube.gateway.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.wetube.gateway.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

//si es options dejar pasar directamente para que el CORS responda
    if (HttpMethod.OPTIONS.equals(request.getMethod())){
        return chain.filter(exchange);
    }

//rutas permitidas sin token
    if (
        path.startsWith("/auth/register") ||
            path.startsWith("/auth/login") ||
            path.startsWith("/auth/refresh") ||
            path.startsWith("/actuator") ||
    path.contains("/v3/api-docs") ||
    path.contains("/swagger-ui") ||
    path.contains("/webjars") ||
    path.startsWith("/storage")){
        return chain.filter(exchange);
    }

    //definicion de rutas publicas GET
    boolean isPublicGet=method.equals("GET") && (path.matches("/videos/interactions/\\d+") ||
        path.matches("/comentarios/\\d+") ||
        path.matches("/like/\\d+/count") ||
        path.matches("/like/\\d+/status") ||
        path.matches("/users/\\d+") ||
        path.matches("/subs/\\d+/count") ||
        path.matches("/subs/\\d+/status") ||
        path.matches("/videos/\\d+/play") ||
        path.equals("/videos/feed") ||
        path.equals("/videos/search")
);

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
String role=jwtUtil.extractRole(token);

if (path.startsWith("/admin") && !"ROLE_ADMIN".equals(role)){
    return unauthorizedResponse(exchange, "acceso denegado, se requieren privilegios de administrador");
}

    String username=jwtUtil.extractUsername(token).orElse("unknown");

    //mutar la peticion para que el token siga y agregar el userId limpio
    ServerWebExchange mutatedExchange=exchange.mutate()
        .request(exchange.getRequest().mutate()
            .header("X-User-Id", userId)
            .header("X-User-Role", role)
            .header("X-User-Name", username)
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

    return response.writeWith(Mono.just(buffer))
        .then(Mono.defer(response::setComplete));
}

@Override
    public int getOrder(){
    return -1;
}

}
