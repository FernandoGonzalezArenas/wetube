'use client'
import { useState } from "react";
import { useRouter } from "next/navigation";
import { Search } from "lucide-react";

export const SearchBar = () => {
const [query, setQuery] = useState('');
const router = useRouter();

const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();

    if(!query.trim()) return;

    //redirigimos a la pagina de resultados
    router.push(`/search?q=${encodeURIComponent(query)}`);
};

return (
    <form onSubmit={handleSearch} className="flex w-full max-w-xl items-center gap-2">
        <div className="relative w-full">
        <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.currentTarget.value)}
        placeholder="buscar..."
        className="w-full rounded-full border border-zinc-700 bg-zinc-900 px-4 py-2 text-white focus:border-blue-500 focus:outline-none"
        />
        <button
        type="submit"
        className="absolute right-3 top-1/2 -translate-y-1/2 text-zinc-400 hover:text-white"
        title="buscar">
        <Search size={20} />    
        </button>
        </div>
    </form>
)
}