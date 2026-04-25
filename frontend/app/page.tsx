/*
componente de la pagina principal
*/
'use client'
import { useQuery } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import VideoCard from "@/components/VideoCard";

export default function Home() {
  const {data: videos, isLoading, error} = useQuery({
    queryKey: ['video-feed'],
    queryFn: () => videoApi.getFeed(),
  });

  if(isLoading) return <div className="p-10 text-center">Cargando videos...</div>
  if (error) return <div className="p-10 text-center text-red-500">error al conectar con el microservicio</div>

  return (
    <main className="p-6 bg-gray-950 min-h-screen">
      <div className="max-w-7xl mx-auto">
      <h1 className="text-3xl font-bold text-white mb-2">Bienvenido a WeTube</h1>
<h2 className="text-2xl font-bold text-white mb-8">Recomendados</h2>

{/* grid de videos: 1 columna en celular, 2 en tablet, 3 o 4 en PC */}
<div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
  {videos?.map((video) => (
    <VideoCard key={video.id} video={video} />
  ))}
</div>
</div>
    </main>
  );
}