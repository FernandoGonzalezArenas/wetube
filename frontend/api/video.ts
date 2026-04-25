import apiClient from "./client";
import { Video, Interactions, VideoPlayback, UpluadUrlResponse } from "@/types/video";

export const videoApi = {
getFeed: async (lastId?: number) => {
    const response = await apiClient.get<Video[]>('/videos/feed', {
        params: {lastId, limit: 10}
    });
    return response.data;
},

getPlaybackInfo: async(videoId: number) => {
const response= await apiClient.get<VideoPlayback>(`/videos/${videoId}/play`);
return response.data;
},

getInteractions: async (videoId: number) => {
    const response= await apiClient.get<Interactions>(`/videos/interactions/${videoId}`);
    return response.data;
},

//obtener la url para el video 
getUploadUrl: async(filename: string) => {
    const response= await apiClient.get<UpluadUrlResponse>('/videos/upload-url', {
        params: { filename }
    }            );
    return response.data;
},

//obtener URL para la miniatura
getThumbUploadUrl: async(filename: string) => {
    const response= await apiClient.get<UpluadUrlResponse>('/videos/upload-tu', {
        params: { filename }
    });
    return response.data;
},

//guardar metadatos en la db
saveMetadata: async(data: { title: string, description: string, duration: number, filename: string, thumbnailUrl: string }) => {
    const response= await apiClient.post<Video>('/videos/save-metadata', data);
    return response.data;
}

}