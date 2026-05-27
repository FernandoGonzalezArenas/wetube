'use client'
import { videoApi } from "@/api/video";
import { useInfiniteQuery } from "@tanstack/react-query";
import { useEffect, useMemo, useRef } from "react";

export default function CommentsList({videoId}: {videoId: number}) {
    const loadMoreRef = useRef<HTMLDivElement>(null);
    const containerRef = useRef<HTMLDivElement>(null);

const {
    data: infiniteInteractionsData, 
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading,
error } = useInfiniteQuery({
    queryKey: ['interactions-comments', videoId],
    queryFn: ({ pageParam }) => videoApi.getInteractions(videoId, pageParam),
    initialPageParam: undefined as number | undefined,
    getNextPageParam: (lastPage) => {
        if(!lastPage || !lastPage.comments || lastPage.comments?.length< 10) return undefined;

        return lastPage.comments[lastPage.comments?.length-1].id;
    },
    staleTime: 1000 * 60 * 5, //5 minutos de cache 
});

const interactions = useMemo(() => {
return infiniteInteractionsData?.pages.flatMap(p => p.comments || []) || [];
}, [infiniteInteractionsData]);

//extraemos el total de comentarios desde el primer objeto de la consulta
const totalComments = infiniteInteractionsData?.pages?.[0]?.totalComments ?? 0;

//intersection observer para el scrol infinito automatico
useEffect(() => {
const container = containerRef.current;
const target = loadMoreRef.current;
if(!target || !container) return;

const observer = new IntersectionObserver((entries) => {
if (entries[0].isIntersecting && hasNextPage && !isFetchingNextPage) {
    fetchNextPage();
}
},
{
    root: container, //el scroll escucha a este div especifico
    threshold: 0.1 
}
 );

    observer.observe(target);

 return () => observer.disconnect();
}, [fetchNextPage, hasNextPage, isFetchingNextPage]);

if(error) return <p className="text-red-500 p-4">Error al cargar comentarios</p>;

if (totalComments === 0 && !isLoading) {
    return <p className="text-gray-500 text-center py-10">se el primero en comentar...</p>;
}

return (    
            <div ref={containerRef} className="flex-1 overflow-y-auto p-4 space-y-4">

                {/* lista de comentarios */}
                {isLoading ? <p className="text-gray-500">Cargando... </p> : 
                interactions?.map((c) => (
                    <article key={c.id} className="flex flex-col border-b border-gray-800/50 pb-2">
                        <h4 className="text-blue-400 text-sm font-bold">{c.usernameAuthor}</h4>
                        <span className="text-gray-500 text-[10px]">{new Date(c.createdAt).toLocaleString()}</span>
                        <p className="text-white text-sm">{c.content}</p>
    </article>
                ))
                }

                {/* elemento invisible que dispara la carga de la siguiente pagina */}
                <div ref={loadMoreRef} className="h-10 w-full flex items-center justify-center">
 {isFetchingNextPage && <p className="text-blue-500 text-xs animate-pulse">Cargando</p>}                   
                </div>
            </div>
    )
}