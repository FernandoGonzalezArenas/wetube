import apiClient from "./client";
import { ReportType, auditorDto, ReportDetail, reportCreateDto } from "@/types/admin";

export const adminApi = {
//crear un reporte
createReport: async (type: ReportType, targetId: number | string, data: reportCreateDto) =>{
        await apiClient.post<string>(`/admin/reports/${type}/${targetId}`, data);
},

getPendingReports: async () => {
    const response = await apiClient.get<ReportDetail[]>('/admin/reports/pending');
    return response.data;
},

dismissReport: async (targetId: number, type: ReportType) => {
   await apiClient.patch<string>(`/admin/reports/${targetId}/dismiss?type=${type}`);
},

deleteVideo: async (videoId: number, data: auditorDto) => {
    await apiClient.delete<string>(`/admin/videos/${videoId}`, { data });
},

banUser: async (userId: number, data: auditorDto) => {
await apiClient.delete<string>(`/admin/users/${userId}`, { data });
}

}