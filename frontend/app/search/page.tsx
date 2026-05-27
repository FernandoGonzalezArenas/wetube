'use client'
import { useRouter, useSearchParams } from "next/navigation";
import { useInfiniteQuery } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import VideoCard from "@/components/VideoCard";
import { Video } from "@/types/video";
import { useEffect, useMemo, useRef } from "react";

export default function SearchPage () {
    const searchParams = useSearchParams();
    const query = searchParams.get('q') || '';
    const videoType = searchParams.get('type') || '';
    const router = useRouter();
    const loadMoreRef = useRef<HTMLDivElement>(null);

    const {data: infiniteData, 
        fetchNextPage,
        hasNextPage,
        isFetchingNextPage,
        isLoading } = useInfiniteQuery({
        queryKey: ['search', query, videoType],
        queryFn: ({ pageParam = 0 }) => videoApi.search(query, videoType, pageParam),
        initialPageParam: 0,
        getNextPageParam: (lastPage) => {
        //en busqueda se usa Page, asi que se busca una nueva pagina de resultados si existe
        return lastPage.last ? undefined : lastPage.number + 1;
        },
        enabled: !!query,
    });

const results = useMemo(() => infiniteData?.pages.flatMap(p => p.content) || [], [infiniteData]);

//observer interseptor para cargar mas contenido
useEffect(() => {
const el = loadMoreRef.current;
if(!el) return;

    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
            fetchNextPage();
        }
    },
{threshold: 0.5});

    observer.observe(el);

return () => observer.disconnect();
}, [fetchNextPage, hasNextPage, isFetchingNextPage]);

const updateFilter = (type: string) => {
router.push(`/search?q=${query}&type=${type}`);
};

    return (
        <div className="p-6">
            <h1 className="mb-4 text-xl font-bold">resultados para: <span className="text-blue-400">"{query}"</span></h1>

            {/* filtros de busqueda */}
            <div className="mb-6 flex gap-4">
                <button
                onClick={() => updateFilter('')}
                className={`px-4 py-2 rounded-lg ${!videoType ? 'bg-white text-black' : 'bg-zinc-800'}`}
                >
                    todo
                </button>

                <button
                onClick={() => updateFilter('videos')}
                className={`px-4 py-2 rounded-lg ${videoType==='videos' ? 'bg-white text-black' : 'bg-zinc-800'}`}
                >
                    videos largos
                </button>

                <button
                onClick={() => updateFilter('shorts')}
                className={`px-4 py-2 rounded-lg ${videoType==='shorts' ? 'bg-white text-black' : 'bg-zinc-800'}`}
                >
                    shorts
                </button>
                        </div>

                        {isLoading ? (
                            <p>Cargando... </p>
                        ) : (
                            <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
{results?.map((video: Video) => (
    <VideoCard key={video.id} video={video} />
))}
        </div>
                        )}

                        {/* div invisible que dispara la siguiente pagina de resultados */}
                        <div ref={loadMoreRef} className="h-10 w-full flex items-center justify-center">
                            {isFetchingNextPage && <p className="text-blue-500 text-xs animate-pulse">Cargando...</p>}
                        </div>
        </div>
    )
}