/*
servicio para el AuthController, conecta los endpoints de el backend con el frontend
*/

import axios from "axios";
import { RegisterRequest, LoginRequest, RefreshTokenRequest, AuthResponse } from '../types/auth';

const api = axios.create({
    baseURL: 'http://localhost:8080/auth',
})

export const authApi = {
register: (data: RegisterRequest) =>
    api.post<string>('/register', data),

login: (data: LoginRequest) =>
    api.post<AuthResponse>('/login', data),

refresh: (data: RefreshTokenRequest) =>
    api.post<AuthResponse>('/refresh', data),

logout: (data: RefreshTokenRequest) =>
    api.post<string>('/logout', data),
}