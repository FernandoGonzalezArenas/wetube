/*
interfases espejo de los DTO's de AuthController
*/

//reflejar el RegisterRequest.java
export interface RegisterRequest {
    username: string;
    password: string;
    role?: 'ROLE_USER' | 'ROLE_ADMIN';
    email: string;
    address: string;
    phone: string;
}

//reflejar el LoginRequest.java
export interface LoginRequest {
    username: string;
    password: string;
}

//reflejar el RefreshTokenRequest 
export interface RefreshTokenRequest {
    refreshToken: string;
}

//reflejar el AuthResponse
export interface AuthResponse {
    accessToken: string;
    refreshToken: string;
}

export interface CustomJwtPayload {
    sub: string;
    userId: string;
    role: string;
    email: string;
    iat: number;
    exp: number;
}