/*
interseptor de peticiones para que el token se adjunte automaticamente a las nuevas solicitudes una vez hecho el login
*/

import axios from 'axios';
import { authApi } from './auth';

//definimos la url principal de acceso al backend
const API_BASE_URL = 'http://localhost:8080';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-type':'application/json',
    },
})

//interseptor para el token
apiClient.interceptors.request.use((config) => {
const token= localStorage.getItem('accessToken');
if(token&&config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
}
return config;
})

apiClient.interceptors.response.use(

(responsse) => responsse, //si la respuesta es 200 no se hace nada
async (error) => {
    const originalRequest=error.config;

    //si el error es 401 y no hemos intentado refrescar ya
    if(error.response?.status === 401 && !originalRequest.url.includes('/auth/refresh') && !originalRequest._retry){
        originalRequest._retry=true;

        try{
//tomamos el refreshToken de el localStorage
const storedRefreshToken=localStorage.getItem('refreshToken');

if(!storedRefreshToken){
    throw new Error('no hay refresh token')
}

//se llama a refresh en el microservicio de auth
const { data } = await authApi.refresh({refreshToken: storedRefreshToken});

//guardamos los nuevos tokens
localStorage.setItem('accessToken', data.accessToken);
localStorage.setItem('refreshToken', data.refreshToken);

//actualizamos el header de la peticion original y la reintentamos
originalRequest.headers.Authorization = `Bearer ${data.accessToken}`;
return apiClient(originalRequest);
        }catch(refreshError){
//si el refreshToken tambien expiro se cierra la sesion
localStorage.removeItem('accessToken');
localStorage.removeItem('refreshToken');
window.location.href = '/login';
return Promise.reject(refreshError);
        }
    }

//si faya el refresh o cualquier otra cosa despues de el reintento
if (error.response?.status === 401) {
    localStorage.clear();
    if(typeof window!== 'undefined') window.location.href = '/login';
}

return Promise.reject(error);
})

export default apiClient;