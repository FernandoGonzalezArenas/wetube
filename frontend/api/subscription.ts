import apiClient from "./client";
import { subsStatus } from "@/types/subscription";

export const subscriptionApi = {
    //dar o quitar subscripcion
    toggleSubscription: async(id: number) => {
        await apiClient.post(`/subs/${id}/toggle`);
    },

    //obtener el status de el boton de subscripcion respecto a un usuario
    getStatusSubscription: async(id: number) => {
        const response = await apiClient.get<subsStatus>(`/subs/${id}/status`);
        return response.data;
    }
}