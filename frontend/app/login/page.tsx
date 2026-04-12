/*
componente de login
*/

'use client'
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { useRouter } from "next/navigation";

export default function LoginPage(){
    const [form, setForm] = useState({ username: '', password: ''});
    const {login, isLoading, error} = useAuth();
const router= useRouter();

const handleSubmit= async(e: React.FormEvent) =>{
 e.preventDefault();
 const success = await login(form);
 if(success) router.push('/') //redirige a el home despues de hacer el login
 }

 return (
<main className="max-w-md mx-auto mt-10 p-6 shadow-lg rounded-lg">
<h1 className="text-2xl font-bold mb-6">iniciar sesion en wetube</h1>

<form onSubmit={handleSubmit} className="flex flex-col gab-4">
    <div>
        <label htmlFor="username" className="block mb-1">nombre de usuario</label>
<input
id="username"
type="text"
required
className="w-full border p-2 rounded text-black"
onChange={(e) => setForm({...form, username: e.target.value})}
/>
    </div>

    <div>
    <label htmlFor="password" className="block mb-1">contraseña</label>
    <input
    id="password"
    type="password"
    required
    className="w-full border p-2 rounded text-black"
    onChange={(e) => setForm({...form, password: e.target.value})}
    />    
    </div>

    {error && (
        <p role="alert" className="text-red-600 font-medium">
            {error}
        </p>
        )}

        <button
        type="submit"
        disabled={isLoading}
        className="bg-blue-600 text-white p-2 rounded hover:bg-blue-700 disabled:bg-gray-400">
            {isLoading? 'cargando...' : 'entrar'}
        </button>
</form>
</main> )
}