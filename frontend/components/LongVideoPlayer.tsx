'use client'
import { useRef, useState, useEffect } from "react";
import { VideoPlayback } from "@/types/video";
import { useQuery, useQueryClient, useMutation } from "@tanstack/react-query";
import { videoApi } from "@/api/video";
import { userApi } from "@/api/user";
import { likesApi } from "@/api/likes";
import SubscribeButton from "./SubscribeButton";
import Image from "next/image";
import Link from "next/link";
import {Play, Pause, RotateCcw, RotateCw, Volume2, VolumeX, Heart, MessageCircle, Share2, User, Send } from "lucide-react";
import CommentsList from "./CommentsList";
import { commentApi } from "@/api/comment";
import ReportButton from "./ReportButton";
import { useAuth } from "@/hooks/useAuth";

interface Props {
    video: VideoPlayback;
}

export default function LongVideoPlayer({video}: Props){
    const queryClient = useQueryClient();
    const [showFullDescription, setShowFullDescription] = useState(false);
const videoRef = useRef<HTMLVideoElement>(null);
const containerRef = useRef<HTMLDivElement>(null);
const inputRef = useRef<HTMLInputElement>(null);
const [isPlaying, setIsPlaying] = useState(true);
const videoId = video.id;
const [isMuted, setIsMuted ] = useState(false);
const [progress, setProgress] = useState(0);
const [duration, setDuration ]= useState(video.duration || 0);
const { isOwner } = useAuth();

useEffect(() => {
const timer = setTimeout(() => {
    containerRef.current?.focus();
}, 100);
return () => clearTimeout(timer);
}, [video.id]);

//consepto de datos para las interacciones
const {data: interactions} = useQuery({
    queryKey: ['interactions-meta', video.id],
    queryFn: () => videoApi.getInteractions(video.id),
});

//consepto de datos para el perfil de el autor
const {data: autor} = useQuery({
    queryKey: ['user_autor', video.userId],
    queryFn: () => userApi.getProfile(video.userId),
});

const isMyVideo = isOwner(video.userId);

//mutacion de el like
const likeMutation = useMutation({
    mutationFn: () => likesApi.toggleLike(video.id),
    onSuccess: () => {
        queryClient.invalidateQueries({queryKey: ['interactions-meta', video.id]});
    }
});

const isLiked = !!interactions?.status?.isLikedByUser;
const totalLikes = interactions?.status?.totalLikes ?? 0;

const btnLike = isLiked ?
`quitar like (a ${totalLikes} personas les gusta esto)` :
`dar like (a ${totalLikes} personas les gusta esto)`;

//mutacion de los comentarios (agregar un nuevo comentario)
const mutation = useMutation({
mutationFn: (content: string) => commentApi.create({videoId, content}),
onSuccess: () => {
    queryClient.invalidateQueries({queryKey: ['interactions-meta', video.id]});
    queryClient.invalidateQueries({queryKey: ['interactions-comments', video.id]});
    if(inputRef.current) inputRef.current.value="";
}
});

//funcion para play / pause
const togglePlay = () => {
if (videoRef.current) {
    if (videoRef.current.paused) {
        videoRef.current.play().catch(() => {});
        setIsPlaying(true);
    }else {
        videoRef.current.pause();
        setIsPlaying(false);
    }
}
}

//actualizar barra de progreso
const handleTimeUpdate = () => {
    if (videoRef.current) {
        setProgress(videoRef.current.currentTime);
    }
}

const handleLoadedMetadata = () => {
if(videoRef.current) setDuration(videoRef.current.duration);
};

const handleSec = (seconds: number) => {
    if (videoRef.current) {
        videoRef.current.currentTime+=seconds;
    }
}

useEffect(() =>{
    const handleKeyDown = (e: KeyboardEvent) => {

if(e.target instanceof HTMLInputElement || e.target instanceof HTMLTextAreaElement) return;

        switch (e.key) {
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
togglePlay();
            break;
            default:
                break;
        }   
    }

        window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);

}, []);

return (
    <div className="min-h-screen bg-black text-white pb-20">
        {/* contenedor de el video */}
    <div className="w-full bg-black sticky top-0 z-10 shadow-2xl">
        <div 
        className="max-w-6xl mx-auto aspect-video bg-gray-900 relative group"
    ref={containerRef} 
            role="application"
    aria-roledescription="reproductor"
    aria-label={video.title}
    tabIndex={0}
>
            <video
            src={video.videoUrl}
            className="w-full h-full cursor-pointer"
            ref={videoRef}
            onClick={togglePlay}
            onTimeUpdate={handleTimeUpdate}
            onLoadedMetadata={handleLoadedMetadata}
            aria-hidden="true"
            autoPlay
            playsInline
            />

{/* controles personalizados */}
            <div className="absolute bottom-0 left-0 right-0 bg-linear-to-t from-black/90 to-transparent p-4 opacity-100 transition-opacity">

{/* barra de progreso */}
<div className="w-full h-1 bg-gray-600 mb-4 rounded-full overflow-hidden">
    <div
    className="h-full bg-red-600 transition-all duration-100"
    style={{width: `${video.duration > 0 ? (progress / video.duration)* 100 : 0}%` }}
    role="progressbar"
    aria-label="progreso de el video"
    aria-valuenow={Math.floor(progress)}
        aria-valuemin={0}
    aria-valuemax={Math.floor(video.duration)}
    aria-valuetext={`${Math.floor(progress)} segundos de ${video.duration}`}
    />
</div>

<div className="flex items-center justify-between">
    <div className="flex items-center gap-4">
        {/* boton play / pause */}
        <button
        onClick={togglePlay}
        className="hover:scale-110 transition-transform"
        aria-label={isPlaying ? "pausar" : "reproducir"}
        title={isPlaying ? "pause" : "reproducir"}
        >
            {isPlaying ? <Pause fill="white" /> : <Play fill="white" />}
        </button>

        {/* retroceder 5 segundos */}
        <button
        onClick={() => handleSec(-5)}
        className="text-gray-300 hover:text-white"
        aria-label="retroceder 5 segundos"
        >
        <RotateCcw size={20} />    
        </button>

        {/* avanzar 5 segundos */}
        <button
        onClick={() => handleSec(5)}
        className="text-gray-300 hover:text-white"
        aria-label="avanzar 5 segundos"
        >
        <RotateCw size={20} />    
        </button>

        {/* boton de mute */}
        <button
        onClick={() => {
            if (videoRef.current) {
                videoRef.current.muted = !isMuted;
                setIsMuted(!isMuted);
            }
        }}
        className="ml-2"
        aria-label={isMuted ? "activar sonido" : "silenciar"}
        title={isMuted ? "activar sonido" : "silenciar"}
        >
            {isMuted ? <VolumeX /> : <Volume2 />}
        </button>
    </div>
</div>

            </div>
        </div>
        </div>

        {/* informacion de el video */}
        <main className="max-w-6xl mx-auto px-4 mt-6">
            {/* titulo grande y resaltado en blanco */}
            <h1 className="text-2xl font-bold leading-tight">{video.title}</h1>

            {/* fila de acciones
            en movil es una torre y en pc es una fila con estacio en medio */}
            <div className="flex flex-col md:flex-row items-center justify-between gap-4 mt-4 py-2 border-b border-gray-800 pb-6">

                {/* lado izquierdo, autor */}
                <div className="flex items-center gap-4">
                                    <Link href={`/profile/${video.userId}`} className="flex items-center gap-4 group">
                                    <div className="w-10 h-10 rounded-full overflow-hidden bg-gray-800 shrink-0">
                                        {autor?.profilePictureUrl ? (
<Image src={autor.profilePictureUrl} alt="" width={40} height={40} className="object-cover" />
                                        ) : (
<div className="w-full h-full flex items-center justify-center bg-blue-700 text-white"><User size={20} /></div>
                                        )}
                                    </div>
                                    <div className="flex flex-col">
                                    <span className="font-bold group-hover:text-blue-400 transition-colors">{autor?.username}</span>
                                    <span className="text-xs text-gray-400">Canal de videos</span>
                                    </div>
                </Link>
                <div className="md:ml-4">
                {video.userId && <SubscribeButton userId={video.userId} />}
</div>
                                </div>

                                {/* lado derecho, botones de interaccion */}
                                <div className="flex items-center gap-2 w-full md:w-auto justify-end">
                                    <button
                                    className={`flex items-center gap-2 px-4 py-2 rounded-full transition-colors ${isLiked ? 'bg-white text-black' : 'bg-gray-800 hover:bg-gray-700'}`}
                                    onClick={() => likeMutation.mutate()}
                                    disabled={likeMutation.isPending}
                                    title={btnLike}
                                    aria-pressed={isLiked}
                                    >
                                        <Heart size={20} className={isLiked ? "fill-current" : ""} />
                                        <span className="font-bold">{totalLikes}</span>
                                    </button>

                                    <button className="flex items-center gap-2 px-4 py-2 bg-gray-800 hover:bg-gray-700 rounded-full" title="compartir">
                                        <Share2 size={20} />
                                    <span className="hidden sm:inline">compartir</span>
                                    </button>

{!isMyVideo && (
            <ReportButton type="VIDEO" targetId={video.id} />                    
)}
            </div>
            </div>

            {/* descripcion */}
            <div className="mt-4 bg-gray-900/50 rounded-xl p-4 border border-gray-800">
            <p className={`text-sm text-gray-200 leading-relaxed ${!showFullDescription && 'line-clamp-3'}`}>
                {video.description || "sin descripcion"}
            </p>
            <button
            onClick={()  => setShowFullDescription(!showFullDescription)}
            className="mt-2 text-sm font-bold text-white hover:underline flex items-center gap-1"
            >
                {showFullDescription ? "ver menos" : "ver mas"}
            </button>
            </div>

            {/* sseccion de comentarios */}
            <div className="mt-10">
                <div className="flex items-center gap-2 mb-6">
                    <MessageCircle size={24} />
                    <h2 className="text-xl font-bold">{interactions?.totalComments || 0} comentarios</h2>
                                    </div>

<form
className="p-4 border-b border-gray-800 flex gap-2"
onSubmit={(e)  => {
    e.preventDefault();
    const val = inputRef.current?.value;
    if(val) mutation.mutate(val);
}}>
<input
ref={inputRef}
type="text"
placeholder="añadir comentario"
className="flex-1 bg-gray-800 text-white p-2 rounded-lg outline-none focus:ring-2 focus:ring-blue-500" />

<button type="submit" title="enviar"
className="text-blue-500 p-2">
    <Send size={20} />
</button>
</form>

{/* seccion de comentarios */}
<CommentsList
videoId={video.id} />
            </div>
        </main>

    </div>
)
}