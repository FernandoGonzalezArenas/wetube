import { Comments } from "./comments";
import { status } from "./likes";

export interface Video {
    id: number;
    userId: number;
    title: string;
    description: string;
    duration: number;
    videoUrl: string;
    thumbnailUrl: string;
    createdAt: string;
}

export interface Interactions {
    comments?: Comments[];
    totalComments?: number;
    status?: status;
}

export interface VideoPlayback {
    id: number;
    userId: number;
    title: string;
    description: string;
    duration: number;
    videoUrl: string;
    thumbnailUrl: string;
    createdAt: string;
}

export interface UpluadUrlResponse {
uploadUrl: string;
finalFileName: string;
}