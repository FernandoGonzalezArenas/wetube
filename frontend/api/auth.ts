/*
servicio para el AuthController, conecta los endpoints de el backend con el frontend
*/

import axios from "axios";
import apiClient from "./client";
import { RegisterRequest, LoginRequest, RefreshTokenRequest, AuthResponse } from '../types/auth';

const publicApi = axios.create({
    baseURL: 'http://localhost:8080/auth',
})

export const authApi = {
register: (data: RegisterRequest) =>
    publicApi.post<string>('/register', data),

login: (data: LoginRequest) =>
    publicApi.post<AuthResponse>('/login', data),

refresh: (data: RefreshTokenRequest) =>
    apiClient.post<AuthResponse>('/auth/refresh', data),

logout: (data: RefreshTokenRequest) =>
    apiClient.post<string>('/auth/logout', data),
}