/*
componente de la pagina principal
*/
'use client'
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import VideoCard from "@/components/VideoCard";
import { userApi } from "@/api/user";
import Link from "next/link";
import Image from "next/image";
import { useAuth } from "@/hooks/useAuth";
import { useEffect, useMemo, useRef } from "react";

export default function Home() {
const { user } = useAuth();
const userId = user?.userId ? Number(user?.userId) : null;
const loadMoreRef = useRef<HTMLDivElement>(null);

const {data: usuario } = useQuery({
queryKey: ['current_user', userId],
queryFn: () => userApi.getProfile(userId!),
enabled: !!userId,
});

  const {data: infiniteData, 
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading, error} = useInfiniteQuery({
    queryKey: ['video-feed'],
    queryFn: ({ pageParam}) => videoApi.getLongsFeed(pageParam),
    initialPageParam: undefined as number | undefined,
    getNextPageParam: (lastPage) => {
      if(lastPage.length<10) return;
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
{threshold: 0.5}
);

  observer.observe(el);

return () => observer.disconnect();
}, [fetchNextPage, hasNextPage, isFetchingNextPage]);

  if (error) return <div className="p-10 text-center text-red-500">error al conectar con el microservicio</div>

  return (
    <main className="p-6 bg-gray-950 min-h-screen">
      <div className="max-w-7xl mx-auto">

        <header className="flex justify-between items-center mb-10 border-b border-gray-800 pb-6">
<div>
      <h1 className="text-3xl font-bold text-white mb-2">Bienvenido a WeTube</h1>
</div>
          {user && usuario && (
       <Link href={`/profile/${userId}`} className="group  flex items-center gap-3 bg-gray-900 hover:bg-gray-800 p-2 pr-4 rounded-full transition-all">
       <div className="relative w-10 h-10 rounded-full overflow-hidden border border-gray-700">
{usuario?.profilePictureUrl ? (
<Image
src={usuario.profilePictureUrl}
alt={usuario.username}
fill
className="object-cover"
/>
) : (
        <div className="w-full h-full flex items-center justify-center font-bold bg-blue-700">
            {usuario?.username?.charAt(0).toUpperCase()}
</div>
)}
       </div>
       <span className="font-medium group-hover:text-blue-400 transition-colors">
       {usuario?.username}
</span>
       </Link>
          )}
          
          </header>
<h2 className="text-2xl font-bold text-white mb-8">Recomendados</h2>

{/* grid de videos: 1 columna en celular, 2 en tablet, 3 o 4 en PC */}
{isLoading ? (
  <div className="p-10 text-center text-gray-400">Cargando videos... </div>
) :(
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
  {videos?.map((video) => (
    <VideoCard key={video.id} video={video} />
  ))}
</div>
)}
</div>

{/* div invisible que dispara la carga de la siguiente pagina de videos */}
<div ref={loadMoreRef} className="h-10 w-full flex items-center justify-center">
  {isFetchingNextPage && <p className="text-blue-500 text-xs animate-pulse">Cargando... </p>}
</div>
    </main>
  );
}