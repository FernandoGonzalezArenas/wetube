    'use client'
    import { useEffect, useRef, useState } from "react";
    import { VideoPlayback } from "@/types/video";
    import { Heart, MessageCircle, Share2, User } from "lucide-react";
import CommentsPanel from "./CommentsPanel";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import { likesApi } from "@/api/likes";
import { createPortal } from "react-dom";

    interface Props {
        video: VideoPlayback;
        onNext: () => void;
        onPrev: () => void;
    }

    export default function VideoPlayer({video, onNext, onPrev}: Props) {
        const containerRef = useRef<HTMLDivElement>(null);
    const videoRef= useRef<HTMLVideoElement>(null);
    const [isPlaying, setIsPlaying] = useState(true);
const [showComments, setShowComments] = useState(false);
const queryClient = useQueryClient();

useEffect(() => {
    const timer = setTimeout(() => {
    //forsamos a el navegador a enfocarse en el reproductor para que asepte teclas
    containerRef.current?.focus();
    }, 100);
    return () => clearTimeout(timer);
}, [video.id]);

//cargamos todo de las interacciones al principio
const {data: interactions, isLoading} = useQuery({
queryKey: ['interactions', video.id],
queryFn: () => videoApi.getInteractions(video.id),
});

//mutacion de el like (usar el microservicio likes directamente)
const likeMutation = useMutation({
    mutationFn: () => likesApi.toggleLike(video.id),
    onSuccess: () => {
        //refrescamos e invalidamos interactions para ver el nuevo conteo y estado
        queryClient.invalidateQueries({queryKey: ['interactions', video.id]});
    }
});

const isLiked=interactions?.status?.isLikedByUser;
const totalLikes=interactions?.status?.totalLikes || 0;

const btnTitle=isLiked ?
`quitar like (a ${totalLikes} personas les gusta esto)` :
`dar like (a ${totalLikes} personas les gusta esto)`;


//usar flechas laterales para atrasar o adelantar el video
const handleSec = (seconds: number) =>{
    if (videoRef.current) {
        videoRef.current.currentTime+=seconds;
    }
}

    useEffect(() => {
        const handleKeyDown = (e: KeyboardEvent) => {
    
//si el panel de comentarios esta mostrandoce (abierto) el reproductor libera las teclas para que el panel las use
if(showComments) return;

//si el usuario esta escribiendo un comentario, no se disparan los controles
if(e.target instanceof HTMLInputElement || e.target instanceof HTMLTextAreaElement) return;

//usamos una variable local para el ref
const player=videoRef.current;
if(!player) return;

            switch(e.key){
                case "ArrowDown":
                    e.preventDefault();
                    onNext();
                    break;
                    case "ArrowUp":
                        e.preventDefault();
                        onPrev();
                        break;
                        case "ArrowRight":
                            e.preventDefault();
                            handleSec(5);
                            break;
                            case "ArrowLeft":
                                e.preventDefault();
                                handleSec(-5);
                                break;
                        case " ":
                            e.preventDefault();
if(videoRef.current){
                            if (videoRef.current.paused) {
videoRef.current.play().catch(() => {}); // Evita el error de "Uncaught (in promise)"                                
 setIsPlaying(true);
                            }else {
                                videoRef.current.pause();
                                setIsPlaying(false);
                            }
                        }
                            break;
                            default:
                                break;
            }
        }

        window.addEventListener("keydown", handleKeyDown);
        return() => {
            window.removeEventListener("keydown", handleKeyDown);
        }
    }, [onNext, onPrev, showComments]);

    return(
        <div className="relative h-screen w-full bg-black flex items-center justify-center overflow-hidden outline-none"
        ref={containerRef}
        role="application" //esto indica que es un widget interactivo
        aria-roledescription="Reproductor" //descripcion de el componente / widget interactivo
        aria-label={video.title}
        tabIndex={0} //muy importante
        >
            {/* contenedor de el video */}
            <video
            ref={videoRef}
            src={video.videoUrl}
            className="h-full w-auto max-w-full"
            aria-hidden="true" //evitamos repeticion de texto
            autoPlay
            loop
            playsInline
            onPlay={() => setIsPlaying(true)}
            onPause={() => setIsPlaying(false)}
            />

{/* controles adicionales para accesibilidad si fueran necesarios */}
<div className="sr-only">
    presiona flechas arriba o abajo para cambiar de video, espacio para pausar o reproducir
</div>

                {/* lateral de interacciones (Accesible por Tab) */}
                <div className="absolute right-4 bottom-20 flex flex-col gap-6 items-center">
                    {/* perfil de el autor */}
                    <button className="p-2 bg-gray-800 rounded-full border border-gray-600 hover:scale-110 transition-transform" title="ver perfil">
                        <User className="text-white" size={28} />
                    </button>

                {/* boton de like */}
                <div className="flex flex-col items-center">
                    <button 
                    className={`p-3 rounded-full hover:bg-red-500/50 transition-colors ${isLiked ? 'bg-red-500' : 'bg-gray-800/50'} ${likeMutation.isPending || isLoading ? 'opacity-50 cursor-not-allowed' : ''}`} 
                    title={btnTitle}
                    disabled={likeMutation.isPending || isLoading} //evita el spam de clicks
                    onClick={(e) => {
                        e.stopPropagation();
                        likeMutation.mutate();
                        containerRef.current?.focus();                        
                    }}
                    aria-pressed={isLiked}
                    >
                    <Heart className={`text-white ${isLiked ? 'fill-current' : ''}`} size={30} />
                    </button>
<span className="text-white text-xs font-bold shadow-sm">{totalLikes || 0}</span>                    
</div>

                    {/* boton de comentarios */}
    <div className="flex flex-col items-center">
        <button 
        className="p-3 bg-gray-800/50 rounded-full hover:bg-blue-500/50 transition-colors" 
        title={`comentarios (${interactions?.totalComments})`}
        onClick={(e) => {
e.stopPropagation();
            setShowComments(true);
        }}
                >
        <MessageCircle className="text-white" size={30} />
        </button>
<span className="text-white text-xs font-bold shadow-sm">{interactions?.totalComments || 0}</span>
    </div>

                {/* boton compartir */}
                <button className="p-3 bg-gray-800/50 rounded-full hover:bg-green-500/50 transition-colors" title="compartir">
                <Share2 className="text-white" size={30} />
                </button>
        </div>

{/* panel de comentarios */}
{showComments && createPortal(
    <CommentsPanel
    videoId={video.id}
    onClose={() => {
        setShowComments(false);
        containerRef.current?.focus();
    }}
    />,
    document.body //se renderiza fuera de el role = "application"
)}

    {/* informacion de el video (abajo a la izquierda) */}
        <div className="absolute bottom-8 left-8 max-w-[80%] text-white">
        <h2 className="text-xl font-bold">{video.title}</h2>
        <p className="text-xs text-gray-300 mt-2 line-clamp-2">{video.description}</p>    
        </div>
                </div>
    )
    }