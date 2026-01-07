package com.wetube.auth.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.wetube.auth.dto.AuthResponse;
import com.wetube.auth.dto.LoginRequest;
import com.wetube.auth.dto.RegisterRequest;
import com.wetube.auth.entity.UserEntity;
import com.wetube.auth.repository.UserRepository;

public class AuthServiceImplTest {

private UserRepository userRepository;
private PasswordEncoder passwordEncoder;
private AuthenticationManager authenticationManager;
private RefreshTokenService refreshTokenService;

private AuthServiceImpl service;

@BeforeEach
void setUp(){
    userRepository=mock(UserRepository.class);
    passwordEncoder=mock(PasswordEncoder.class);
    authenticationManager=mock(AuthenticationManager.class);
    refreshTokenService=mock(RefreshTokenService.class);
    service=new AuthServiceImpl(userRepository, passwordEncoder, authenticationManager, refreshTokenService);
}

@Test
void register_usuarioYaExiste_400(){
    //el repositorio indica que el usuario ya existe
    when(userRepository.findByUsername("fernando")).thenReturn(Optional.of(new UserEntity()));

    //armamos el request
    RegisterRequest req=new RegisterRequest();
    req.setUsername("fernando");

    //esperamos codigo de estado 400
    ResponseStatusException ex=assertThrows(ResponseStatusException.class, () -> service.register(req));

    //comprobamos el error de la respuesta
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
}

@Test
void register_OK_guardaConPasswordCodificado(){
    //no existe el usuario
    when(userRepository.findByUsername("mario")).thenReturn(Optional.empty());

    //encoder retorna ENC
    when(passwordEncoder.encode("12345678")).thenReturn("ENC");

    //creamos el objeto register
    RegisterRequest req=new RegisterRequest();
   req.setUsername("mario");
   req.setPassword("12345678");
   req.setEmail("mario@mail.com");
   service.register(req);
   
   //capturamos el UserEntity guardado
   ArgumentCaptor<UserEntity> cap=ArgumentCaptor.forClass(UserEntity.class);
   verify(userRepository).save(cap.capture());
   UserEntity saved=cap.getValue();
   assertEquals("mario", saved.getUsername());
   assertEquals("ENC", saved.getPassword());
   assertEquals("mario@mail.com", saved.getEmail());
}

@Test
void login_OK_autentica_y_generaTokens(){
    //refreshTokenService devuelve tokens
    when(refreshTokenService.generateTokensForUser("fernando")).thenReturn(new AuthResponse("ACC", "REF"));

    //crear objeto LoginRequest con credenciales
    LoginRequest req=new LoginRequest();
    req.setUsername("fernando");
    req.setPassword("secret");
AuthResponse resp=service.login(req);

//comprobar que los tokens no son nulos
assertNotNull(resp.getAccessToken());
assertNotNull(resp.getRefreshToken());

//se devuelven los tokens generados
assertEquals("ACC", resp.getAccessToken());
assertEquals("REF", resp.getRefreshToken());

//verificamos que se registro el refreshToken
verify(refreshTokenService).registerRefresh("REF", "fernando");

//verificamos que el usuario se autentico
verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("fernando", "secret"));
}

@Test
void login_credencialesInvalidas_401(){
    //simula credenciales invalidas
    doThrow(new BadCredentialsException("bad"))
    .when(authenticationManager).authenticate(any());

    //crear objeto LoginRequest con malas credenciales
    LoginRequest req=new LoginRequest();
    req.setUsername("fernando");
    req.setPassword("oops");

    ResponseStatusException ex=assertThrows(ResponseStatusException.class, () -> service.login(req));
assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
}

@Test
void login_errorGenerico_500(){
    //generar el error 500 de el servidor
    doThrow(new RuntimeException("boom"))
    .when(authenticationManager).authenticate(any());

//crear objecto login para error 500
LoginRequest req=new LoginRequest();
req.setUsername("fernando");
req.setPassword("oops");

//comprobar el error arrojado
ResponseStatusException ex=assertThrows(ResponseStatusException.class, () -> service.login(req));
assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
}

}
