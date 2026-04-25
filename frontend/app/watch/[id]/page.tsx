'use client'
import { useParams, useRouter } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import VideoPlayer from "@/components/VideoPlayer";
import { useEffect, useCallback } from "react";

export default function WatchPage(){
    const { id } = useParams();
    const router= useRouter();
    const videoId = Number(id);

//obtenemos la lista de videos para saber cual es el siguiente / anterior
const {data: videos} = useQuery({
queryKey: ['video-feed'],
queryFn: () => videoApi.getFeed(),
});

    const {data: currentVideo, isLoading } = useQuery({
        queryKey: ['video-playback', videoId],
        queryFn: () => videoApi.getPlaybackInfo(videoId),
        enabled: !!videoId,
    });

//precarga de el video siguiente
useEffect(() => {
    if (videos && videoId) {
        const currentIndex = videos.findIndex(v => v.id === videoId);
        const nextVideo = videos[currentIndex+1];

        if (nextVideo) {
            //precargamos la ruta de el siguiente video
            router.prefetch(`/watch/${nextVideo.id}`);

//precarga el archivo de video en la cache de el navegador
const link=document.createElement('link');
link.rel='prefetch';
link.as='video';
link.href=nextVideo.videoUrl;
document.head.appendChild(link);

            console.log(`precargando datos de la ruta /watch/${nextVideo.id}`);

            //eliminar el elemento link cuando el componente se desmonte o el ID cambie
            return () => {
                if (document.head.contains(link)) {
                    document.head.removeChild(link);
                }
            }
        }
    }
}, [videos, videoId, router]);

const handleNext = useCallback(() => {
    if(!videos) return;
    const currentIndex = videos.findIndex(v => v.id === videoId);
    if (currentIndex < videos.length-1) {
        router.replace(`/watch/${videos[currentIndex+1].id}`);
    }else {
        router.replace(`/watch/${videos[0].id}`);
    }
}, [videos, videoId, router]);

const handlePrev= useCallback(() => {
    if(!videos) return;
    const currentIndex=videos.findIndex(v => v.id === videoId);
    if (currentIndex > 0) {
        router.replace(`/watch/${videos[currentIndex-1].id}`);
    }
}, [videos, videoId, router]);

    if(isLoading || !currentVideo) return <div className="h-screen bg-black" />

    return(
        <main className="h-screen w-full bg-black flex items-center justify-center overflow-hidden">
            {/* pasamos la URL firmada que viene de el backend a el componente */}
            <VideoPlayer
            key={currentVideo.id}
            video={currentVideo}
            onNext={handleNext}
            onPrev={handlePrev}
            />
        </main>
    )
}