'use client'
import { useParams } from "next/navigation";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { userApi } from "@/api/user";
import { videoApi } from "@/api/video";
import VideoCard from "@/components/VideoCard";
import { Heart, Lock } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import { useMemo, useEffect, useRef } from "react";
import Link from "next/link";

export default function LikedVideosPage () {
    const { id } = useParams();
    const userId = Number(id);
    const { isOwner } = useAuth();
    const isMyProfile = isOwner(userId);
    const loadMoreRef = useRef<HTMLDivElement>(null);

    //obtener perfil para verificar configuraciones de privacidad
    const {data: perfil, isLoading: loadingUser } = useQuery({
queryKey: ['user', userId],
queryFn: () => userApi.getProfile(userId)
    });

 const canSeeLikes = isMyProfile || perfil?.privacyLikes;

 const { data: infiniteData,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading: loadingVideos
 } = useInfiniteQuery({
    queryKey: ['user-liked-videos', userId],
    queryFn: ({ pageParam }) => videoApi.getLikedVideos(userId, pageParam),
    initialPageParam: undefined as number | undefined,
    getNextPageParam: (lastPage) => {
        if(!lastPage || lastPage.length<10) return;

        return lastPage[lastPage.length-1].id;
    },
    enabled: !!canSeeLikes,
 });

const videos = useMemo(() => infiniteData?.pages.flat() || [], [infiniteData]);

useEffect(() => {
    const el = loadMoreRef.current;
    if(!el || !canSeeLikes) return;

    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
            fetchNextPage();
        }
    },
{threshold: 0.3}
);

observer.observe(el);
return () => observer.disconnect();
}, [fetchNextPage, hasNextPage, isFetchingNextPage, canSeeLikes]);

if(loadingUser) return <div className="p-10 text-white text-center">Cargando...</div>;

if (!canSeeLikes) {
    return(
    <div className="min-h-screen bg-black text-white flex flex-col items-center justify-center p-6 text-center">
        <Lock size={64} className="text-gray-600 mb-4" />
        <h2 className="text-2xl font-bold">Lista de reproduccion no disponible</h2>
<p className="text-gray-400 mt-2 max-w-sm">el usuario <span className="text-blue-600">{perfil?.username}</span> ha decidido mantener su lista de videos gustados en privado</p>
<Link href={`/profile/${userId}`} className="mt-6 text-blue-400 hover:underline">volver al perfil de <span className="text-blue-600">{perfil?.username}</span></Link>
    </div>
    )
}

return (
    <div className="min-h-screen bg-black text-white pt-24 px-4 max-w-6xl mx-auto">
<div className="flex items-center gap-3 border-b border-gray-800 pb-4 mb-8">
<Heart className="text-red-500 fill-red-500" size={28} />
<h1 className="text-3xl font-extrabold">videos que me gustan</h1>
</div>

{loadingVideos ? (
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
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
<Heart size={48} className="mx-auto mb-4 opacity-20" />
<p>aun no tienes videos que te gusten</p>
    </div>
)}
    </div>
)

}