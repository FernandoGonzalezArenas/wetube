/*
aqui se guardan los tokens en el almacenamiento local de el frontend
*/

import { useState, useEffect, useCallback } from "react";
import { authApi } from "../api/auth";
import { RegisterRequest, LoginRequest } from "../types/auth";
import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { CustomJwtPayload } from "../types/auth";

export const useAuth = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
const [user, setUser ] = useState<CustomJwtPayload | null>(null);

const logout = useCallback(() => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setUser(null);
}, []);

//funcion para decodificar y validar el token
const decodeAndSetUser = useCallback((token: string) => {
try {
const decoded = jwtDecode<CustomJwtPayload>(token);

//verificamos expiracion
if (decoded.exp*1000 < Date.now()) {
    logout();
    return null;
}

setUser(decoded);
return decoded;
}catch(error) {
    setUser(null);
    return null;
}
}, [logout]);

//al cargar la app revisamos si hay un token valido
useEffect(() => {
const token = localStorage.getItem('accessToken');
if (token) {
    decodeAndSetUser(token);
}
}, [decodeAndSetUser]);

    const login = async(data: LoginRequest) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await authApi.login(data);
            //guardamos los tokens obtenidos con el login en LocalStorage
            localStorage.setItem('accessToken', response.data.accessToken);
            localStorage.setItem('refreshToken', response.data.refreshToken);

            //decodificamos el token inmediatamente para que la app sepa la identidad de el usuario
            decodeAndSetUser(response.data.accessToken);
            return true;
    }catch(err: unknown){
        if(axios.isAxiosError(err)){
setError(err.response?.data?.message || 'usuario o contraseña incorrectos');
        }else{
            setError('ocurrio un error inesperado');
        }
return false;
    } finally {
setIsLoading(false);
    }
    }

const register = async(data: RegisterRequest) => {
 setIsLoading(true);
 setError(null);
 
 try {
await authApi.register(data);
return true;
 }catch(err: unknown){
    if(axios.isAxiosError(err)){
setError(err.response?.data || 'error en el registro');
    }else{
        setError('ocurrio un error inesperado');
    }
return false;
 }finally{
setIsLoading(false);
 }
}

return {
    user,
    logout,
    register, 
    login, 
    isLoading, 
    error,
//helper para saber si un usuario es el dueño
 isOwner: (resourceUserId: string | number) => {
    return user?.userId === String(resourceUserId);
 }
};
}