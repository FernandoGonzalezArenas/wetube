import apiClient from "./client";
import { user, uploadUrlResponse } from "@/types/user";

export const userApi = {
    //obtener un perfil
    getProfile: async (id: number) => {
        const response = await apiClient.get<user>(`/users/${id}`);
        return response.data;
    },

    //actualizar el perfil
    updateProfile:  async(data: {bio: string, profilePictureUrl: string, privacyLikes: boolean, privacySubs: boolean}) => {
        const response = await apiClient.put<user>('/users/me', data);
        return response.data;
    },

 //obtener URL firmada para subir la foto de perfil
 uploadUrlProfile: async(filename: string) => {
    const response = await apiClient.get<uploadUrlResponse>('/users/upload-ppu', {
        params: {filename}
    });
    return response.data;
 }   
}