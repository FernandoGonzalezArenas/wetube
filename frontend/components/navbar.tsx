/*
barra de navegacion para aparecer en la parte superior de las paginas
*/

'use client'
import Link from "next/link";
import { useRouter } from "next/navigation";
import { authApi } from "@/api/auth";
import { SearchBar } from "./SearchBar";
import { useAuth } from "@/hooks/useAuth";
import { videoApi } from "@/api/video";
import { Zap } from "lucide-react";
import { useState } from "react";

export default function Navbar(){
const router= useRouter();
const {user, logout } = useAuth();
const [isLoginShorts, setIsLoginShorts] = useState(false);

//estado de el login
 const isLoggedIn = !!user;

const goToRandomShort = async() => {
    if(isLoginShorts) return;
    setIsLoginShorts(true);
    
    try {
        const shorts = await videoApi.getShortsFeed();
        if (shorts && shorts.length > 0) {
            //seleccionar uno al azar de el array de shorts
            const randomIndex = Math.floor(Math.random() * shorts.length);
const shortId= shorts[randomIndex].id;

router.push(`/watch/${shortId}?type=shorts`);
}
}catch(error) {
    console.error("error al cargar shorts: ", error);
} finally {
    setIsLoginShorts(false);
}
};

const handleLogout =async () => {
    const refreshToken=localStorage.getItem('refreshToken');
    if(refreshToken){
        try{
//se llama a el logout de el useAuth
await authApi.logout({ refreshToken });
        }catch(error){
console.error("error al cerrar sesion en el servidor ", error);
        }
    }

logout();

router.refresh();
router.push('/');

}

return (
    <nav className="flex justify-between items-center p-4 bg-gray-900 text-white shadow-md">

        <Link href="/" className="text-2xl font-bold text-red-500">Wetube</Link>

{/* boton de shorts */}
<button
onClick={goToRandomShort}
disabled={isLoginShorts}
className={`flex items-center gap-2 hover:text-red-400 transition-colors font-medium ${isLoginShorts ? 'opacity-50 cursor-not-allowed' : ''}`}
>
    <Zap size={20} fill="currentColor" />
    <span className="hidden sm:inline">shorts</span>
</button>
        <div className="flex gap-4">
            {isLoggedIn ? (
                <>
                <Link href="/upload" className="hover:text-gray-300">subir video</Link>
                <button
                onClick={handleLogout} className="bg-red-600 px-3 py-1 rounded hover:bg-red-700">
                    cerrar sesion
                </button>
                </>
                        ) : (
                            <>
                            <Link href="/login" className="hover:text-gray-300">iniciar sesion</Link>
                            <Link href="/register" className="border border-white px-3 py-1 rounded hover:bg-white hover:text-black transition">registrarse</Link>
                            </>
                        )}
        </div>
        <SearchBar />
    </nav>
)
}