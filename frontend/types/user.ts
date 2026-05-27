export interface user {
    id: number;
    username: string;
    bio: string;
    profilePictureUrl: string;
    privacyLikes: boolean;
    privacySubs: boolean;
    createdAt: string;
}

export interface uploadUrlResponse {
    uploadUrl: string;
    filename: string;
}