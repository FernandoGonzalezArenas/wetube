'use client'
import { useState, useRef } from "react";
import { userApi } from "@/api/user";
import { useQueryClient } from "@tanstack/react-query";
import { X, User, Camera, Loader2, Eye, EyeOff } from "lucide-react";
import Image from "next/image";
import { user } from "@/types/user";
import { createPortal } from "react-dom";

interface Props {
    perfil: user;
    onClose: () => void;
}

export default function EditProfileModal({perfil, onClose }: Props) {
    const [bio, setBio] = useState(perfil.bio || "");
    const [isUploading, setIsUploading] = useState(false);
    const [imagePreview, setImagePreview ] = useState<string | null>(null);
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [privacyLikes, setPrivacyLikes] = useState(perfil.privacyLikes ?? false);
    const [privacySubs, setPrivacySubs] = useState(perfil.privacySubs ?? false);
const fileInputRef =useRef<HTMLInputElement>(null);

const queryClient = useQueryClient();

    const handleSave = async () => {

        setIsUploading(true);

        try {
let finalImageUrl = perfil.profilePictureUrl;

if(selectedFile){
    const profileUpload = await userApi.uploadUrlProfile(selectedFile.name);
    await fetch(profileUpload.uploadUrl, {
        method: 'PUT',
        body: selectedFile,
        headers: {'Content-Type': selectedFile.type}
    }) 
finalImageUrl = profileUpload.filename;
}
//actualizar los datos de el perfil
await userApi.updateProfile({
bio,
profilePictureUrl: finalImageUrl,
privacyLikes,
privacySubs
});

//refrescar datos y cerrar el modal de edicion
queryClient.invalidateQueries({ queryKey: ['user', perfil.id] });
onClose();
        }catch(error) {
console.error("error actualizando perfil: ", error);
alert("error guardando los cambios");
        } finally {
setIsUploading(false);
        }
            }

const currentImage = imagePreview || perfil?.profilePictureUrl || null;

const modalContent =(
        <div className="w-screen h-screen fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm p-4">
        <div className="bg-gray-900 border border-gray-800 w-full max-w-md rounded-2xl overflow-hidden shadow-2xl">
<div className="flex items-center justify-between p-4 border-b border-gray-800">
    <h2 className="text-xl font-bold">Editar perfil</h2>
    <button onClick={onClose} title="cerrar" className="p-2 hover:bg-gray-800 rounded-full transition-colors">
    <X size={24} />    
    </button>
</div>

<div className="p-6 space-y-6 overflow-y-auto flex-1 custom-scrollbar">
    {/* cambio de foto */}
    <div className="flex flex-col items-center gap-4">
<input
type="file"
ref={fileInputRef}
className="hidden"
accept="image/*"
onChange={(e) => {
    const file = e.target.files?.[0];
    if (file) {
        if(imagePreview) URL.revokeObjectURL(imagePreview);
        setSelectedFile(file);
        setImagePreview(URL.createObjectURL(file));
    }
}}
/>
<button
type="button"
onClick={() => fileInputRef.current?.click()}
className="relative group w-28 h-28 rounded-full focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2 focus:ring-offset-gray-900 transition-all"
aria-label="actualizar foto de perfil">
<div className="relative w-full h-full rounded-full overflow-hidden border-4 border-blue-600">
    {currentImage ? (
    <Image
 src={currentImage}
 alt="preview"
 fill
 className="object-cover"
 unoptimized
 />
    ) : (
        <div 
        className="w-full h-full flex items-center justify-center bg-gray-800"
>
<User size={48} className="text-gray-600" />
            </div>
    )}   
</div>
<div className="absolute inset-0 flex items-center justify-center bg-black/40 opacity-0 hover:opacity-100 rounded-full transition-opacity cursor-pointer">
<Camera className="text-white" size={32} />
</div>
</button>
<span className="text-xs text-gray-400">has click para cambiar la foto</span>
    </div>

{/* biografía */}
<div className="space-y-2">
    <label className="text-sm font-medium text-gray-400">biografía</label>
    <textarea
    value={bio}
    onChange={(e) => setBio(e.target.value)}
    className="w-full bg-gray-800 border border-gray-700 rounded-xl p-3 text-white focus:ring-2 focus:ring-blue-500 outline-none h-32 resize-none"
    placeholder="cuentale al mundo sobre ti..."
    />
</div>

{/* ajustes de privacidad */}
<div className="space-y-4 pt-2 border-t border-gray-800">
    <h3 className="text-sm font-semibold text-gray-400 uppercase tracking-wider">ajustes de privacidad</h3>

    {/* conmutador videos gustados */}
    <div className="flex items-center justify-between p-3 bg-gray-800/40 border border-gray-800 rounded-xl">
    <div className="flex items-start gap-3">
        {privacyLikes ? 
        <Eye className="text-blue-400 mt-0.5 shrink-0" size={18} /> : 
        <EyeOff className="text-gray-500 mt-0.5 shrink-0" size={18} />}
        <div>
            <p className="text-sm font-medium text-white">videos que te gustan</p>
            <p className="text-xs text-gray-500">{privacyLikes ? 'cualquiera puede ver tu lista de videos gustados' : 'solo tu puedes ver tu lista de videos gustados'}</p>
        </div>
    </div>
<label htmlFor="checkPrivLikes" className="relative inline-flex items-center cursor-pointer select-none">
    <input
    id="checkPrivLikes"
    type="checkbox"
    checked={privacyLikes}
    onChange={(e) => setPrivacyLikes(e.target.checked)}
    className="sr-only peer"
    aria-label={privacyLikes ? 'ocultar lista de videos gustados' : 'mostrar lista de videos gustados'}
    />
    <div className="w-11 h-6 bg-gray-700 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white
    after:content-[''] after:absolute after:top-2px after:left-2px after:bg-gray-300 after:border-gray-300 after:border after:rounded-full after:w-5 after:h-5 after:transition-all peer-checked:bg-blue-600 peer-checked:after:bg-white"></div>
</label>
    </div>

{/* privacidad de subscripciones */}
    <div className="flex items-center justify-between p-3 bg-gray-800/40 border border-gray-800 rounded-xl">
    <div className="flex items-start gap-3">
        {privacySubs ? 
        <Eye className="text-blue-400 mt-0.5 shrink-0" size={18} /> : 
        <EyeOff className="text-gray-500 mt-0.5 shrink-0" size={18} />}
        <div>
            <p className="text-sm font-medium text-white">tus subscripciones</p>
            <p className="text-xs text-gray-500">{privacySubs ? 'cualquiera puede ver tu lista de videos de subscripciones' : 'solo tu puedes ver tu lista de videos de subscripciones'}</p>
        </div>
    </div>
<label htmlFor="checkPrivSubs" className="relative inline-flex items-center cursor-pointer select-none">
    <input
    id="checkPrivSubs"
    type="checkbox"
    checked={privacySubs}
    onChange={(e) => setPrivacySubs(e.target.checked)}
    className="sr-only peer"
    aria-label={privacySubs ? 'ocultar lista de subscripciones' : 'mostrar lista de subscripciones'}
    />
    <div className="w-11 h-6 bg-gray-700 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white
    after:content-[''] after:absolute after:top-2px after:left-2px after:bg-gray-300 after:border-gray-300 after:border after:rounded-full after:w-5 after:h-5 after:transition-all peer-checked:bg-blue-600 peer-checked:after:bg-white"></div>
</label>
    </div>

    </div>
</div>

<div className="p-4 bg-gray-800/50 flex gap-3">
    <button
    onClick={onClose}
    className="flex-1 py-2.5 rounded-xl font-bold hover:bg-gray-700 transition-colors"
    >
        cancelar
    </button>
    <button
    onClick={handleSave}
    disabled={isUploading}
    className="flex-1 py-2.5 bg-blue-600 hover:bg-blue-500 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl font-bold transition-colors flex items-center justify-center gap-2"
    >
        {isUploading && <Loader2 className="animate-spin" size={18} />}
        guardar
    </button>
</div>

        </div>
    </div>
);

//solo ejecutar en el cliente
if(typeof window === "undefined") return;

return createPortal(modalContent, document.body);

}