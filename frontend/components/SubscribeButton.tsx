'use client'
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { subscriptionApi } from "@/api/subscription";

interface Props {
    userId: number;
    onActionComplete?: () => void;
}

export default function SubscribeButton({ userId, onActionComplete}: Props) {
 const queryClient = useQueryClient();
 
 const {data: status, isLoading } = useQuery({
queryKey: ['sub-status', userId],
queryFn: () => subscriptionApi.getStatusSubscription(userId),
 });

 //mutacion para el comportamiento de el boton de subscripcion
 const mutation = useMutation({
mutationFn: () => subscriptionApi.toggleSubscription(userId),
onSuccess: () => {
    queryClient.invalidateQueries({ queryKey: ['sub-status', userId]});
    if(onActionComplete) onActionComplete();
}
 });

 if(isLoading) return <div className="h-9 w-24 bg-gray-800 animate-pulse rounded-full" />

 return (
    <button
    onClick={() => mutation.mutate() }
    disabled={mutation.isPending}
    className={`px-6 py-2 rounded-full font-bold text-sm transition-all ${
        status?.subscriptionByUser ?
        'bg-gray-800 text-gray-300 hover:bg-gray-700' :
        'bg-white text-black hover:scale-105 active:scale-95'
    }`}
    >
        {status?.subscriptionByUser ? 'subscrito' : 'subscribirse'}
    </button>
 )
}