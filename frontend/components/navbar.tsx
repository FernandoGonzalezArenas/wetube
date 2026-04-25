/*
barra de navegacion para aparecer en la parte superior de las paginas
*/

'use client'
import Link from "next/link";
import { useRouter } from "next/navigation";
import { authApi } from "@/api/auth";

export default function Navbar(){
const router= useRouter();

//calcular el login... si esta en el servidor es false
const isLoggedIn = !!localStorage.getItem('accessToken');

const handleLogout =async () => {
    const refreshToken=localStorage.getItem('refreshToken');
    if(refreshToken){
        try{
//se llama a el logout de el AuthController
await authApi.logout({ refreshToken });
        }catch(error){
console.error("error al cerrar sesion en el servidor ", error);
        }
    }

//limpiesa local (Command Pattern)
localStorage.removeItem('accessToken');
localStorage.removeItem('refreshToken');

router.refresh();
router.push('/login');

//window.location.href = '/login';
}

return (
    <nav className="flex justify-between items-center p-4 bg-gray-900 text-white shadow-md">

        <Link href="/" className="text-2xl font-bold text-red-500">Wetube</Link>
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
    </nav>
)
}