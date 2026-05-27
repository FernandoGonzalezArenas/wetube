import { useEffect, useRef } from "react";
import { X, Send } from "lucide-react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { commentApi } from "@/api/comment";
import { createPortal } from "react-dom";
import CommentsList from "./CommentsList";

interface Props {
    videoId: number;
    onClose: () => void;
}

export default function CommentsPanel({videoId, onClose}: Props) {
const queryClient = useQueryClient();
const inputRef = useRef<HTMLInputElement>(null);
const panelRef = useRef<HTMLDivElement>(null);

//enfocamos el panel al abrir para que resiba las teclas
useEffect(() => {

const timer = setTimeout(() => {
panelRef.current?.focus();
}, 50);
return () => clearTimeout(timer);
}, []);

const handleKeyDown= (e: React.KeyboardEvent) => {
    //evitamos que las teclas se propaguen a los componentes padres
    e.stopPropagation();

    //tecla escape para salir de el panel
    if (e.key === "Escape") {
        onClose();
    }
};

const mutation = useMutation({
    mutationFn: (content: string) => commentApi.create({videoId, content}),
    onSuccess: () => {
        queryClient.invalidateQueries({queryKey: ['interactions-comments', videoId]});
        queryClient.invalidateQueries({queryKey: ['interactions-meta', videoId]});
        if(inputRef.current) inputRef.current.value = "";
    }
});

const modalContent = (
    <div
    ref={panelRef}
    tabIndex={0} //permite que el div resiba foco por el tab
    onKeyDown={handleKeyDown}
    role="dialog" //define esto como un dialogo modal
    aria-labelledby="comments_title" //ancla el ID de el hencabezado H3 de el titulo de comentarios
    aria-modal="true" //le indica a el foco que debe quedarce aqui
    className="fixed right-0 top-0 h-full w-full md:w-96 bg-gray-900 shadow-2xl z-50 flex flex-col animate-in slide-in-from-right duration-300 outline-none">
        {/* Header */}
        <div className="p-4 border-b border-gray-800 flex justify-between items-center">
            <h3 id="comments_title" className="text-white font-bold text-lg">comentarios</h3>
            <button onClick={onClose} title="cerrar" className="text-gray-400 hover:text-white">
                <X size={24} />
            </button>
        </div>

        {/* formulario superior */}
        <form
        className="p-4 border-b border-gray-800 flex gap-2"
        onSubmit={(e) => {
            e.preventDefault();
            const val = inputRef.current?.value;
            if(val) mutation.mutate(val);
        }}>
        <input
        ref={inputRef}
        type="text"
        placeholder="Añadir comentario"
        className="flex-1 bg-gray-800 text-white p-2 rounded-lg outline-none focus:ring-2 focus:ring-blue-500" />
        <button type="submit" title="enviar" className="text-blue-500 p-2">
            <Send size={20} />
            </button>    
        </form>

{/* lista de comentarios */}
<CommentsList
videoId={videoId} />
    </div>
);

if(typeof window === "undefined") return;

return createPortal(modalContent, document.body);
}