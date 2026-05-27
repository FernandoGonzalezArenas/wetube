import apiClient from "./client";
import { Video, Page, Interactions, VideoPlayback, UpluadUrlResponse } from "@/types/video";

export const videoApi = {
getShortsFeed: async (lastId?: number) => {
    const response = await apiClient.get<Video[]>('/videos/shorts-feed', {
        params: {lastId, limit: 10}
    });
    return response.data;
},

getLongsFeed: async (lastId?: number) => {
    const response = await apiClient.get<Video[]>('/videos/long-feed', {
        params: {lastId, limit: 10}
    });
    return response.data;
},

search: async (keyword: string, type?: string, page?: number) => {
    const response = await apiClient.get<Page<Video>>('/videos/search', {
        params: {keyword, type, page, size: 10}
    });
    return response.data;
},

getPlaybackInfo: async(videoId: number) => {
const response= await apiClient.get<VideoPlayback>(`/videos/${videoId}/play`);
return response.data;
},

getInteractions: async (videoId: number, lastId?: number) => {
    const response= await apiClient.get<Interactions>(`/videos/interactions/${videoId}`, {
        params: {lastId, limit: 10}
    });
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
},

//obtener los videos subidos por el usuario
getVideosByUser: async (userId: number, lastId?: number) => {
    const response = await apiClient.get<Video[]>(`/videos/${userId}`, {
        params: {lastId, limit: 10}
    });
    return response.data;
},

getLikedVideos: async (userId: number, lastId?: number) => {
    const response = await apiClient.get<Video[]>(`/videos/list-likes/${userId}`, {
        params: {lastId, limit: 10}
    });
    return response.data;
},

getSubsChannelVideos: async (userId: number, lastId?: number) => {
    const response = await apiClient.get<Video[]>(`/videos/my-feed-subs/${userId}`, {
        params: {lastId, limit: 10}
    });
    return response.data;
}

}