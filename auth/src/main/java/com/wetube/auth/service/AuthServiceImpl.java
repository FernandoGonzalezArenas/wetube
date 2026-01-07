package com.wetube.auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wetube.auth.dto.AuthResponse;
import com.wetube.auth.dto.LoginRequest;
import com.wetube.auth.dto.RegisterRequest;
import com.wetube.auth.entity.UserEntity;
import com.wetube.auth.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
private final AuthenticationManager authenticationManager;
private final RefreshTokenService refreshTokenService;

public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService){
    this.userRepository=userRepository;
    this.passwordEncoder=passwordEncoder;
    this.authenticationManager=authenticationManager;
this.refreshTokenService=refreshTokenService;
}

@Override
public void register(RegisterRequest request){
    if (userRepository.findByUsername(request.getUsername()).isPresent()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el usuario ya existe");
    }

    UserEntity user=new UserEntity();
    user.setUsername(request.getUsername());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEmail(request.getEmail());
    user.setAddress(request.getAddress());
    user.setPhone(request.getPhone());
    userRepository.save(user);
}

@Override
public AuthResponse login(LoginRequest request){
try {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

    //se crean los tokens
AuthResponse newTokens=refreshTokenService.generateTokensForUser(request.getUsername());

//se registra el refresh token en la base de datos
refreshTokenService.registerRefresh(newTokens.getRefreshToken(), request.getUsername());

//se retornan los nuevos tokens
    return newTokens;
}catch (BadCredentialsException e){
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "usuario o contraseña incorrectos");
}catch (Exception e){
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error en la autenticacion");
}
}

}
