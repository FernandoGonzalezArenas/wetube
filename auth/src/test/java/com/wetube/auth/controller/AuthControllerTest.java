package com.wetube.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetube.auth.dto.AuthResponse;
import com.wetube.auth.dto.LoginRequest;
import com.wetube.auth.dto.RefreshTokenRequest;
import com.wetube.auth.dto.RegisterRequest;
import com.wetube.auth.security.JwtUtil;
import com.wetube.auth.service.AuthService;
import com.wetube.auth.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class) //carga solo la capa MVC de el controlador
@AutoConfigureMockMvc(addFilters=false) //se desactivan los filtros de seguridad para esta prueba
public class AuthControllerTest {

@Autowired MockMvc mvc; //mock mvc para peticiones simuladas
@Autowired ObjectMapper mapper; //mapper para la serializacion Json

//mockear dependencias de el controlador
@MockBean AuthService authService;
@MockBean RefreshTokenService refreshTokenService;
@MockBean JwtUtil jwtUtil;
@MockBean UserDetailsService userDetailsService;

@Test
void  register_201() throws  Exception{
//no retorna nada, solo verificamos el registro 201
    doNothing().when(authService).register(any(RegisterRequest.class));

    RegisterRequest req=new RegisterRequest();
    req.setUsername("fernando");
    req.setPassword("12345678");
    req.setEmail("fer@mail.com");

mvc.perform(post("/auth/register")
.contentType(MediaType.APPLICATION_JSON)
.content(mapper.writeValueAsString(req)))
.andExpect(status().isCreated())
.andExpect(content().string("usuario creado correctamente"));
    }

@Test
void  login_200_devuelveTokens() throws  Exception{
when(authService.login(any(LoginRequest.class)))
.thenReturn(new AuthResponse("ACC", "REF"));

LoginRequest req=new LoginRequest();
req.setUsername("fernando");
req.setPassword("12345678");

mvc.perform(post("/auth/login")
.contentType(MediaType.APPLICATION_JSON)
.content(mapper.writeValueAsString(req)))
.andExpect(status().isOk())
.andExpect(jsonPath("$.accessToken").value("ACC"))
.andExpect(jsonPath("$.refreshToken").value("REF"));
}

@Test
void  refresh_200_devuelveTokens() throws  Exception{
when(refreshTokenService.refreshToken("REF_TOKEN_VALIDO")).thenReturn(new AuthResponse("NEW_ACC", "NEW_REF"));

    RefreshTokenRequest req=new RefreshTokenRequest();
    req.setRefreshToken("REF_TOKEN_VALIDO");

mvc.perform(post("/auth/refresh")
.contentType(MediaType.APPLICATION_JSON)
.content(mapper.writeValueAsString(req)))
.andExpect(status().isOk())
.andExpect(jsonPath("$.accessToken").value("NEW_ACC"))
.andExpect(jsonPath("$.refreshToken").value("NEW_REF"));
}

@Test
void  logout_200_ok() throws  Exception{
doNothing().when(refreshTokenService).deleteByToken(anyString());

RefreshTokenRequest req=new RefreshTokenRequest();
req.setRefreshToken("REF_TOKEN_VALIDO");

mvc.perform(post("/auth/logout")
.contentType(MediaType.APPLICATION_JSON)
.content(mapper.writeValueAsString(req)))
.andExpect(status().isOk())
.andExpect(content().string("sesion cerrada correctamente"));
}

@Test
@DisplayName("debe fayar (400) si el refreshToken esta en blanco")
void refresh_tokenBlanco_400() throws Exception{
    RefreshTokenRequest req=new RefreshTokenRequest();
    req.setRefreshToken("");

    mvc.perform(post("/auth/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());
}

@Test
void  register_datosInvalidos_400() throws  Exception{
    RegisterRequest req=new RegisterRequest();
    req.setUsername("yo");
    req.setPassword("123");
    req.setEmail("mi correo");

    mvc.perform(post("/auth/register")
.contentType(MediaType.APPLICATION_JSON)
.content(mapper.writeValueAsString(req)))
.andExpect(status().isBadRequest())
.andExpect(jsonPath("$.fieldErrors.username").exists())
.andExpect(jsonPath("$.fieldErrors.email").exists());
}

}
