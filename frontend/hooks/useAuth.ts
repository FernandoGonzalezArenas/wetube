/*
aqui se guardan los tokens en el almacenamiento local de el frontend
*/

import { useState } from "react";
import { authApi } from "../api/auth";
import { RegisterRequest, LoginRequest } from "../types/auth";
import axios from "axios";

export const useAuth = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const login = async(data: LoginRequest) => {
        setIsLoading(true);
        setError(null);
        try {
            const response = await authApi.login(data);
            //guardamos los tokens obtenidos con el login en LocalStorage
            localStorage.setItem('accessToken', response.data.accessToken);
            localStorage.setItem('refreshToken', response.data.refreshToken);
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

return {register, login, isLoading, error};
}