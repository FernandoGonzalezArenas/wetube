'use client'
import { useParams } from "next/navigation";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { userApi } from "@/api/user";
import { videoApi } from "@/api/video";
import { subscriptionApi } from "@/api/subscription";
import Image from "next/image";
import VideoCard from "@/components/VideoCard";
import SubscribeButton from "@/components/SubscribeButton";
import { CalendarDays, Video as VideoIcon, Settings, Camera } from "lucide-react";
import { useAuth } from "@/hooks/useAuth";
import EditProfileModal from "@/components/EditProfileModal";
import { useEffect, useMemo, useRef, useState } from "react";
import Link from "next/link";
import ReportButton from "@/components/ReportButton";

export default function ProfilePage(){
    const { id } = useParams();
const userId = Number(id);
const {user, isOwner} = useAuth();
const isMyProfile =!!user ? isOwner(userId) : false;
const [isEditModalOpen, setIsEditModalOpen] = useState(false);
const loadMoreRef = useRef<HTMLDivElement>(null);

console.log(`identificador de el perfil solicitado = ${userId}\nperfil propio o anonimo = ${isMyProfile}`);

//obtener los datos de el usuario
const {data: perfil, isLoading: loadingUser } = useQuery({
    queryKey: ['user', userId],
    queryFn: () => userApi.getProfile(userId),
});

//obtener videos de el usuario
const { data: infiniteData, 
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading: loadingVideos } = useInfiniteQuery({
    queryKey: ['user-videos', userId],
    queryFn: ({  pageParam}) => videoApi.getVideosByUser(userId, pageParam),
    initialPageParam: undefined as number | undefined,
    getNextPageParam: (lastPage) => {
        if(lastPage.length < 10) return;

        return lastPage[lastPage.length-1].id;
    },
});

const videos = useMemo(() => {
    return infiniteData?.pages.flat() || [];
}, [infiniteData]);

useEffect(() => {
const el = loadMoreRef.current;
if(!el) return;

    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
            fetchNextPage();
        }
    },
{
    threshold: 0.3
});

    observer.observe(el);

return () => observer.disconnect();
}, [fetchNextPage, hasNextPage, isFetchingNextPage, videos]);

//obtener estadisticas de subscripcion
const {data: stats } = useQuery({
    queryKey: ['sub-status', userId],
    queryFn: () => subscriptionApi.getStatusSubscription(userId),
});

if(loadingUser) return <div className="p-10 text-white">Cargando perfil de usuario...</div>;

return (
 <div className="min-h-screen bg-black text-white">

{/* modal de edicion */}
{isEditModalOpen && perfil && (
    <EditProfileModal
    perfil={perfil}
    onClose={() => setIsEditModalOpen(false)}
    />
    )}

    {/* HEADER DE EL PERFIL */}
    <div className="w-full max-w-6xl mx-auto pt-20 px-4">
    <div className="flex flex-col md:flex-row items-center md:items-start gap-8 border-b border-gray-800 pb-10">
{/* foto de perfil en grande */}
<div className="group relative w-40 h-40 shrink-0 rounded-full overflow-hidden bg-gray-800 border-4 border-gray-900 shadow-xl">
    {perfil?.profilePictureUrl ? (
 <Image
 src={perfil.profilePictureUrl}
 alt={perfil.username}
 fill
        className="object-cover"
        />
    ):(
        <div className="w-full h-full flex items-center justify-center text-5xl font-bold bg-blue-700">
            {perfil?.username?.charAt(0).toUpperCase()}
</div>
    )}
{/* overlay rapido si eres dueño */}
{isMyProfile && (
    <div 
    onClick={() => setIsEditModalOpen(true)}
    className="absolute inset-0 bg-black/40 flex items-center justify-center opacity-0 group-hover:opacity-100 cursor-pointer transition-opacity">
<Camera size={32} />
</div>
)}
</div>

{/* informacion y bio */}
<div className="flex-1 flex flex-col gap-4 text-center md:text-left">
<div>
    <h1 className="text-4xl font-extrabold">{perfil?.username}</h1>
    <div className="flex items-center justify-center md:justify-start gap-4 mt-2 text-gray-400 text-sm">
        <span className="font-medium text-white">{stats?.totalSubscriptions || 0} subscriptores</span>
        <span>•</span>
        <span className="flex items-center gap-1">
            <VideoIcon size={16} /> {videos?.length || 0} videos
        </span>
    </div>
<div className="flex justify-center md:justify-start gap-2">
    {isMyProfile ? (
<button
onClick={() => setIsEditModalOpen(true)}
className="flex items-center gap-2 px-6 py-2 bg-gray-800 hover:bg-gray-700 rounded-full font-bold text-sm  transition-all border border-gray-700"
>
<Settings size={16} />
editar perfil    
</button>
    ) : (
<>
<SubscribeButton userId={userId} />
     <ReportButton type="USER" targetId={userId} />                    
</>
 )}   
</div>
    </div>    

<p className="text-gray-300 max-w-2xl leading-relaxed">
    {perfil?.bio || "este usuario aun no agrega una biografía"}
</p>

<div className="flex items-center justify-center md:justify-start gap-2 text-gray-500 text-xs italic">
<CalendarDays size={14} /> se unio el {new Date(perfil?.createdAt || '').toLocaleDateString()}    
</div>

</div>
        </div>    

{/* pestañas de secciones de el perfil */}
<div className="flex flex-row border-b border-gray-800 mt-6 gap-6 text-sm font-semibold">
    {(isMyProfile || perfil?.privacyLikes) && (
        <Link href={`/profile/${userId}/liked`} className="text-gray-400 hover:text-white pb-3 px-1 transition-colors">
            videos gustados
        </Link>
    )}

    {(isMyProfile || perfil?.privacySubs) && (
        <Link href={`/profile/${userId}/subscriptions`} className="text-gray-400 hover:text-white pb-3 px-1 transition-colors">
            subscripciones
        </Link>
    )}
</div>

{/* seccion de videos */}
<div className="mt-10">
    <h2 className="text-2xl font-bold mb-6 border-b-2 border-white w-fit pb-1">videos de este canal</h2>

    {loadingVideos ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
{[1, 2, 3].map(n => <div key={n} className="aspect-video bg-gray-800 animate-pulse rounded-xl" />)}
</div>
    ) : videos && videos.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-x-6 gap-y-10">
{videos.map((v) => (
    <VideoCard key={v.id} video={v} />
))} 

<div ref={loadMoreRef} className="h-10 w-full flex items-center justify-center">
    {isFetchingNextPage && <p className="text-blue-500 text-xs animate-pulse">Cargando</p>}
</div>
</div>
    ) : (
        <div className="py-20 text-center text-gray-500">
            <VideoIcon size={48} className="mx-auto mb-4 opacity-20" />
<p>este canal aun no tiene contenido</p>            
</div>
    )}
</div>
    </div>
 </div>   
)
}