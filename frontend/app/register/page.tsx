/*
componente de register
*/

'use client'
import { useState } from "react";
import { useAuth } from "@/hooks/useAuth";
import { RegisterRequest } from "@/types/auth";

export default function RegisterPage(){
    const [form, setForm] = useState<RegisterRequest>({
        username: '',
        password: '',
        email: '',
        address: '',
        phone: '',
    })
    const {register, isLoading, error} = useAuth();

    const handleSubmit= async (e: React.FormEvent) => {
        e.preventDefault();
        const success= await register(form);
        if(success) alert("usuario creado, ahora puedes iniciar sesion.");
    }

    return (
        <main className="max-w-md mx-auto mt-10 p-6 shadow-lg rounded-lg">
            <h1 className="text-2xl font-bold mb-6">crear cuenta</h1>

            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                {/* campo username */}
                <div>
<label htmlFor="reg-username">nombre de usuario (min 4 caracteres)</label>
<input
id="reg-username"
type="text"
className="w-full border p-2 rounded text-black"
onChange={(e) => setForm({...form, username: e.target.value})}
/>
                    </div>

                    {/* campo password */}
<div>
<label htmlFor="reg-password">contraseña</label>
<input
id="reg-password"
type="password"
className="w-full border p-2 rounded text-black"
onChange={(e) => setForm({...form, password: e.target.value})}
/>
</div>

                {/* campo email */}
            <div>
<label htmlFor="email">correo electronico</label>
<input
id="email"
type="email"
required
className="w-full border p-2 rounded text-black"
onChange={(e) => setForm({...form, email: e.target.value})}
/>
                </div>

{/* campo phone */}
<div>
 <label htmlFor="-phone">telefono (10 digitos)</label>
 <input
 id="phone"
 type="tel"
 className="w-full border p-2 rounded text-black"
 onChange={(e) => setForm({...form, phone: e.target.value})}
 />   
</div>

{/* campo direccion */}
<div>
<label htmlFor="address">direccion fisica</label>
<input
id="address"
type="text"
className="w-full border p-2 rounded text-black"
onChange={(e) => setForm({...form, address: e.target.value})}
/>    
</div>

{error && <p role="alert" className="text-red-600">{error}</p>}

<button
type="submit"
disabled={isLoading}
className="bg-green-600 text-white p-2 rounded disabled:bg-gray-400">{isLoading ? 'registrando...' : 'registrarse'}
</button>
</form>
        </main>
    )
}