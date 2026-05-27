'use client'
import Link from "next/link";
import Image from "next/image";
import { Video } from "@/types/video";
import { useQuery } from "@tanstack/react-query";
import { userApi } from "@/api/user";

interface Props {
    video: Video;
}

export default function VideoCard({ video }: Props){
const {data: perfil} = useQuery({
    queryKey: ['user', video.userId],
    queryFn: () => userApi.getProfile(video.userId),
    staleTime: 1000*60*10, //cache de 10 minutos
})

const handleDuration = (tiempo: number): string => {
const horas: number = Math.floor(tiempo / 3600);
const minutos: number = Math.floor((tiempo % 3600) / 60);
const segundos: number = tiempo % 60;

//formateo para que siempre tengan 2 digitos
const h = horas > 0 ? `${horas.toString().padStart(2, '0')}: ` : "";
const m = `${minutos.toString().padStart(2, '0')}:`;
const s= segundos.toString().padStart(2, '0');

return `${h}${m}${s}`;
}

const videoType = video.duration < 60 ? 'shorts' : 'videos';

return (
    <div className="flex flex-col w-full gap-3">
    <Link href={`/watch/${video.id}?type=${videoType}`} className="group block">
   <div className="relative aspect-video overflow-hidden rounded-xl bg-gray-800">
    <Image
    src={video.thumbnailUrl}
    alt={video.title}
    fill
    className="object-cover transition-transform duration-300 group hover:scale-105"
    sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
    />
    </div>
    <div className="mt-2">
        <h3 className="font-semibold text-white line-clamp-2 leading-snug group-hover:text-blue-400 transition-colors">{video.title} <span className="text-gray-400 text-xs ml-1">{handleDuration(video.duration)}</span></h3>
        </div>     
    </Link>

    <div className="flex flex-col gap-1">
<Link href={`/profile/${video.userId}`} className="flex items-center gap-3 w-fit">
<div className="relative w-9 h-9 shrink-0 overflow-hidden rounded-full bg-gray-800 border border-gray-700">
    {perfil?.profilePictureUrl ? (
<Image
  src={perfil?.profilePictureUrl}
  alt={perfil?.username}
  fill
  className="object-cover transition-opacity group hover:opacity-80"
    />
    ) : (
<div className="w-full h-full bg-blue-600 flex items-center justify-center text-white font-bold text-xs">
{perfil?.username?.charAt(0).toUpperCase()}
</div>    
    )}
</div>
    <p className="text-sm font-medium text-gray-300 group-hover:text-white transition-colors">{perfil?.username || "Cargando..."}</p>
</Link>
</div>

        <p className="text-xs text-gray-500 line-clamp-1 pl-12">{video.description.substring(0, 60)}...</p>
</div>
)
}