import apiClient from "./client";
import { Comments } from "@/types/comments";

export const commentApi = {
    //obtener comentarios con paginacion
    getByVideo: async (videoId: number) => {
        const response = await apiClient.get<Comments[]>(`/comentarios/${videoId}`);
        return response.data;
    },

//crear nuevo comentario
create: async (data: {videoId: number, content: string}) => {
    const response = await apiClient.post<Comments>('/comentarios', data);
    return response.data;
},

//obtener el numero de comentarios de un video
getCountByVideo: async(videoId: number) => {
    const response = await apiClient.get<number>(`/comentarios/${videoId}/count`);
    return response.data;
}

}