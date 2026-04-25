/*
formulario de subida de archivos.
*/

'use client';
import { useState } from "react";
import { videoApi } from "@/api/video";
import { useRouter } from "next/navigation";

export default function UploadPage() {
    const [title, setTitle] = useState("");
    const [description, setDescription] =useState("");
    const [videoFile, setVideoFile] = useState<File | null>(null);
    const [thumbFile, setThumbFile] = useState<File | null>(null);
    const [isUploading, setIsUploading] = useState(false);
    const router = useRouter();

const getVideoDuration = (file: File): Promise<number> => {
    return new Promise((resolve, reject) => {
        const video= document.createElement('video');
        video.preload = 'metadata';
        video.onloadedmetadata = () => {
            window.URL.revokeObjectURL(video.src);
            resolve(video.duration); //duracion en segundos
        }
        video.onerror = () => reject("error al cargar los metadatos del video");
        video.src = URL.createObjectURL(file);
    });
}

const handleUpload = async (e: React.FormEvent) => {
    e.preventDefault();

    if(!videoFile || !thumbFile) return alert("selecciona los 2 archivos");

    setIsUploading(true);
    try {
//se obtienen las 2 URL firmadas (video y miniatura)
const videoData = await videoApi.getUploadUrl(videoFile.name);
const thumbData = await videoApi.getThumbUploadUrl(thumbFile.name);

//subir archivos directo a minio con fetch nativo porque axios a veces agrega headers que rompen la firma
await fetch(videoData.uploadUrl, {
    method: 'PUT',
    body: videoFile,
    headers: {'Content-Type': videoFile.type}
});

await fetch(thumbData.uploadUrl, {
    method: 'PUT',
    body: thumbFile,
    headers: { 'Content-Type': thumbFile.type}
});

//obtener la duracion de el video
const durationFloat = await getVideoDuration(videoFile);
const duration=Math.round(durationFloat);

//guardar metadatos en la base de datos
await videoApi.saveMetadata({
    title,
    description,
    duration,
    filename: videoData.finalFileName,
    thumbnailUrl: thumbData.finalFileName
});

alert("video subido con exito");
router.push("/");
    }catch(error){
console.error(error);
alert("error al subir el video");
} finally {
    setIsUploading(false);
}
}

return(
    <main className="min-h-screen bg-gray-950 text-white p-8">
        <div className="max-w-2xl mx-auto bg-gray-900 p-8 rounded-2xl border border-gray-800">
            <h1 className="text-3xl font-bold mb-6">Subir nuevo video</h1>

            <form onSubmit={handleUpload} className="space-y-6">
                <div>
                <label className="block text-sm mb-2">titulo</label>
                    <input
                    className="w-full bg-gray-800 border border-gray-700 p-3 rounded-lg focus:outline focus:border-blue-500"
                    value={title}
                    onChange={e => setTitle(e.target.value)}
                    required
                    />
                </div>

                <div>
                    <label className="block text-sm mb-2">descripcion</label>
                    <textarea
                    className="w-full bg-gray-800 border border-gray-700 p-3 rounded-lg h-32"
                    value={description}
                    onChange={e => setDescription(e.target.value)}
                    />
                </div>
<div className="grid grid-cols-1 md:grid-cols-2 gap-4">
<div className="p-4 border-2 border-dashed border-gray-700 rounded-xl">
<label className="block text-sm mb-2 text-blue-400">elegir archivo de video</label>
<input
type="file"
accept="video/*"
onChange={e => setVideoFile(e.target.files?.[0] || null)}
/>
    </div>    
<div className="p-4 border-2 border-dashed border-gray-700 rounded-xl">
    <label className="block text-sm mb-2 text-green-400">elegir archivo de miniatura para el video</label>
    <input
    type="file"
    accept="image/*"
    onChange={e => setThumbFile(e.target.files?.[0] || null)}
    />
</div>
</div>

<button
type="submit"
disabled={isUploading}
className={`w-full p-4 rounded-xl font-bold transition-colors ${isUploading? 'bg-gray-700' : 'bg-green-600 hover:bg-blue-700'}`}
>
    {isUploading? "subiendo..." : "publicar video"}
</button>
            </form>
        </div>
    </main>
)
}