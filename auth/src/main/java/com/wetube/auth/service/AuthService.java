package com.wetube.auth.service;

import com.wetube.auth.dto.AuthResponse;
import com.wetube.auth.dto.LoginRequest;
import com.wetube.auth.dto.RegisterRequest;

public interface AuthService {

    public void register(RegisterRequest request);

    public AuthResponse login(LoginRequest request);

    void banUser(Long userId);

}
