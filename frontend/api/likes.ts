import apiClient from "./client";
import { status } from "@/types/likes";

export const likesApi = {
//dar o quitar like
toggleLike: async(videoId: number) => {
    await apiClient.post(`/like/${videoId}/toggle`);
},

//obtener el status de el like en el video
getStatus: async(videoId: number) => {
    const response = await apiClient.get<status>(`/like/${videoId}/status`);
    return response.data;
}

}
