'use client'
import Link from "next/link";
import Image from "next/image";
import { Video } from "@/types/video";

interface Props {
    video: Video;
}

export default function VideoCard({ video }: Props){
return (
    <Link href={`/watch/${video.id}`} className="group cursor-pointer">
   <div className="relative aspect-video overflow-hidden rounded-xl bg-gray-800">
    <Image
    src={video.thumbnailUrl}
    alt={video.title}
    fill
    className="object-cover transition-transform group-hover:scale-105"
    sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
    />
    </div>
    <div className="mt-3">
        <h3 className="font-semibold text-white line-clamp-2">{video.title}</h3>
        <p className="text-sm text-gray-400 mt-1">{video.description.substring(0, 60)}...</p>
        </div>     
    </Link>
)
}