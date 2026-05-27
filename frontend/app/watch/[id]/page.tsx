'use client'
import { useParams, useRouter, useSearchParams } from "next/navigation";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import VideoPlayer from "@/components/VideoPlayer";
import { useEffect, useCallback, useMemo } from "react";
import LongVideoPlayer from "@/components/LongVideoPlayer";

export default function WatchPage(){
    const { id } = useParams();
    const router= useRouter();
    const videoId = Number(id);
const searchParams = useSearchParams();
const videoType = searchParams.get('type');

//obtenemos la lista de videos segun el tipo de la solicitud
const {
    data: infiniteData,
fetchNextPage,
hasNextPage,
isFetchingNextPage } = useInfiniteQuery({
queryKey: ['video-feed', videoType],
queryFn: ({ pageParam }) => {
    return videoType === 'shorts' ?
videoApi.getShortsFeed(pageParam)
: videoApi.getLongsFeed(pageParam);
},
initialPageParam: undefined as number | undefined,
//esta funcion determina cual es el siguiente lastId
getNextPageParam: (lastPage) => {
    if(lastPage.length<10) return undefined; //no hay mas si la ultima pagina es menor al limite
    return lastPage[lastPage.length-1].id; //el id de el ultimo video resibido
},
});

// aplanamos las paginas para tener una sola lista de videos
const videos = useMemo(() => {
return infiniteData?.pages.flat() || [];
}, [infiniteData]);

//cargar mas videos automaticamente
useEffect(() => {
if (videos.length>0 && videoId) {
    const currentIndex = videos.findIndex(v => v.id === videoId);

    //si el usuario esta cerca de el fin de los videos de la pagina y hay mas paginas disponibles se carga la siguiente
    if (currentIndex >= videos.length-2 && hasNextPage && !isFetchingNextPage) {
        fetchNextPage();
    }
}
}, [videoId, videos, hasNextPage, isFetchingNextPage, fetchNextPage]);

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
            router.prefetch(`/watch/${nextVideo.id}?type=${videoType}`);

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
}, [videos, videoId, router, videoType]);

const handleNext = useCallback(() => {
    if(videos.length===0) return;
    const currentIndex = videos.findIndex(v => v.id === videoId);
    if (currentIndex < videos.length-1) {
        router.replace(`/watch/${videos[currentIndex+1].id}?type=${videoType}`);
    }else {
        router.replace(`/watch/${videos[0].id}?type=${videoType}`);
    }
}, [videos, videoId, router, videoType]);

const handlePrev= useCallback(() => {
    if(videos.length===0) return;
    const currentIndex=videos.findIndex(v => v.id === videoId);
    if (currentIndex > 0) {
        router.replace(`/watch/${videos[currentIndex-1].id}?type=${videoType}`);
    }
}, [videos, videoId, router, videoType]);

    if(isLoading || !currentVideo) return <div className="h-screen bg-black" />

const isShort = currentVideo.duration < 60;

    return(
        <main className="h-screen w-full bg-black flex items-center justify-center overflow-hidden">
            {/* pasamos la URL firmada que viene de el backend a el componente */}
            {isShort || videoType === 'shorts' ? (
            <VideoPlayer
            key={currentVideo.id}
            video={currentVideo}
            onNext={handleNext}
            onPrev={handlePrev}
            />
            ) : (
<LongVideoPlayer
video={currentVideo}
/>
            )}
        </main>
    )
}