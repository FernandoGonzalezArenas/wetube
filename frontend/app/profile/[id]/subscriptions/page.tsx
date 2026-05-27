'use client'
import { useParams } from "next/navigation";
import { useQuery, useInfiniteQuery } from "@tanstack/react-query";
import { useMemo, useEffect, useRef } from "react";
import { useAuth } from "@/hooks/useAuth";
import { userApi } from "@/api/user";
import { videoApi } from "@/api/video";
import VideoCard from "@/components/VideoCard";
import Link from "next/link";
import { Tv, Lock } from "lucide-react";

export default function SubscriptionsPage() {
 const { id } = useParams();
 const userId = Number(id);
 const { isOwner} = useAuth();
 const isMyProfile = isOwner(userId);
const loadMoreRef = useRef<HTMLDivElement>(null);

const {data: perfil, isLoading: loadingUser} = useQuery({
queryKey: ['user', userId],
queryFn: () => userApi.getProfile(userId),
});

const canSeeSubs = isMyProfile || perfil?.privacySubs;

const { data: infiniteData,
    fetchNextPage,
    hasNextPage, 
    isFetchingNextPage,
    isLoading: loadingVideos
} = useInfiniteQuery({
queryKey: ['user-subs-videos', userId],
queryFn: ({ pageParam }) => videoApi.getSubsChannelVideos(userId, pageParam),
initialPageParam: undefined as number | undefined,
getNextPageParam: (lastPage) => {
    if(!lastPage || lastPage.length<10) return;

    return lastPage[lastPage.length-1].id;
},
enabled: !!canSeeSubs,
});

const videos = useMemo(() => infiniteData?.pages.flat() || [], [infiniteData]);

useEffect(() => {
    const el = loadMoreRef.current;
    if(!el || !canSeeSubs) return;

    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
            fetchNextPage();
        }
    },
{threshold: 0.3});

observer.observe(el);
return () => observer.disconnect();
}, [fetchNextPage, hasNextPage, isFetchingNextPage, canSeeSubs]);

if(loadingUser) return <div className="p-10 text-white text-center">Cargando...</div>;

if (!canSeeSubs) {
    return (
        <div className="min-h-screen bg-black text-white flex flex-col items-center justify-center p-6 text-center">
            <Lock size={64} className="text-gray-600 mb-4" />
            <h2 className="text-2xl font-bold">subscripciones privadas</h2>
            <p className="text-gray-400 mt-2 max-w-sm">las subscripciones de <span className="text-blue-600">{perfil?.username}</span> son privadas</p>
            <Link href={`/profile/${userId}`} className="mt-6 text-blue-400 hover:underline">volver al perfil de <span className="text-blue-600">{perfil?.username}</span></Link>
        </div>
    )
}

return (
    <div className="min-h-screen bg-black text-white pt-24 px-4 max-w-6xl mx-auto">
<div className="flex items-center gap-3 border-b border-gray-800 pb-4 mb-8">
    <Tv className="text-blue-500" size={28} />
    <h1 className="text-3xl font-extrabold">subscripciones</h1>
</div>

{loadingVideos ? (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
{[1, 2, 3].map(n => <div key={n} className="aspect-video bg-gray-800 animate-pulse rounded-xl" />)}
    </div>
) : videos.length>0 ? (
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-x-6 gap-y-10">
{videos.map((v) => <VideoCard key={v.id} video={v} />)}

<div ref={loadMoreRef} className="h-10 w-full flex items-center justify-center">
{isFetchingNextPage && <p className="text-blue-500 text-xs animate-pulse">Cargando...</p>}
     </div>   
    </div>
) : (
    <div className="py-20 text-center text-gray-500">
<Tv size={48} className="mx-auto mb-4 opacity-20" />
<p>aun no hay contenido de subscripciones para mostrar...</p>
    </div>
)}
    </div>
)

}