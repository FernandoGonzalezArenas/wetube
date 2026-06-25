'use client'
import React, { useEffect, useRef, useState } from "react";
import { adminApi } from "@/api/admin";
import { PREDEFINED_REASONS, ReportType } from "@/types/admin";
import { Flag } from "lucide-react";
import { createPortal } from "react-dom";

interface ReportButtonProps {
    type: ReportType;
    targetId: number | string;
    onlyIcon?: boolean;
    onActionComplete?: () => void;
}

export default function ReportButton ({type, targetId, onlyIcon, onActionComplete}: ReportButtonProps){
const [isOpen, setIsOpen] = useState(false);
const [selectedReason, setSelectedReason] = useState(PREDEFINED_REASONS[0].value);
const [description, setDescription] = useState("");
const [isSubmitting, setIsSubmitting] =useState(false);
const reportRef= useRef<HTMLDivElement>(null);

//enfocamos el panel al abrir para que resiba las teclas
useEffect(() => {
if(isOpen) return;

const timer = setTimeout(() => {
reportRef.current?.focus();
}, 50);
return () => clearTimeout(timer);
}, [isOpen]);

const handleKeyDown = (e: React.KeyboardEvent) => {
    //se detiene la propagacion para que el reproductor de shorts no capture las teclas
    e.stopPropagation();

    //se sale de el modal con la tecla Escape
    if (e.key === 'Escape') {
        setIsOpen(false);
        if(onActionComplete) onActionComplete();
    }
};

const handleReport = async (e: React.FormEvent) => {
    e.preventDefault();

    setIsSubmitting(true);
    try {
await adminApi.createReport(type, targetId,  {reason: selectedReason, description });
alert("reporte creado con exito");
setIsOpen(false);
setDescription("");
if(onActionComplete) onActionComplete();
    } catch(error) {
console.error("error al crear el reporte: ", error);
alert("no se pudo crear el reporte");
    } finally {
 setIsSubmitting(false);       
    }
    }

if(typeof window === "undefined") return null;

return (
    <>
{/* boton desencadenador */}
<button
onClick={() => {
    setIsOpen(true);
    reportRef.current?.focus();
}}
className={onlyIcon 
?    "p-3 bg-gray-800/50 rounded-full hover:bg-red-500/50 transition-colors"
: "flex items-center gap-2 px-3 py-1.5 bg-gray-800 hover:bg-gray-700 rounded-full text-sm text-gray-300 font-medium transition"}
title={`reportar ${type === 'VIDEO' ? 'Video' : 'Usuario'}`}
>
    <Flag size={16} />
{!onlyIcon && <span>reportar { type === 'VIDEO' ? 'Video' : 'Usuario'}</span> }
</button>
{/* modal para reportar */}
{isOpen && createPortal (
    <div 
    ref={reportRef}
    tabIndex={0}
    onKeyDown={handleKeyDown} //detiene borbujeo de teclas hacia el reproductor
    role="dialog"
    aria-labelledby="report_title" //soluciona el error de accesibilidad de el dialog
    aria-modal="true" //indica a los lectores de pantalla que el foco pertenece al modal
    className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4"
    >
        <div className="bg-gray-900 border border-gray-800 rounded-xl p-6 w-full max-w-md shadow-2xl text-white">
            <h3 id="report_title" className="text-xl font-bold mb-4 flex items-center gap-2">
<Flag className="text-red-500" size={20} />
reportar {type === 'VIDEO' ? 'Video' : 'Usuario'}
            </h3>

            <form onSubmit={handleReport} className="flex flex-col gap-4">
                <div>
                    <label className="block text-sm text-gray-400 mb-2">Selecciona el motivo del reporte</label>

<select
value={selectedReason}
onChange={(e) => setSelectedReason(e.target.value)}
className="w-full bg-gray-800 text-white p-3 rounded-lg border border-gray-700 focus:outline-none focus:border-red-500 text-sm"
>
    {PREDEFINED_REASONS.map((reason) =>(
        <option key={reason.value} value={reason.value}>{reason.label}</option>
    ))}
</select>
                </div>

<div>
    <label className="block text-sm text-gray-400 mb-2">descripcion detallada (opcional)</label>
    <textarea
    value={description}
    onChange={(e) => setDescription(e.target.value)}
    placeholder="proporciona mas contexto sobre el reporte"
    rows={3}
    className="w-full bg-gray-800 text-white p-3 rounded-lg border border-gray-700 focus:outline-none focus:border-red-500 text-sm resize-none"
    />
</div>

                <div className="flex justify-end gap-3 text-sm font-semibold">
                 <button
                 type="button"
                 disabled={isSubmitting}
                 onClick={() => {
                    setIsOpen(false);
                if(onActionComplete) onActionComplete();
            }}
                 className="px-4 py-2 bg-gray-800 hover:bg-gray-700 rounded-lg transition"
                 >
                    cancelar
                    </button>

                    <button
                    type="submit"
                    disabled={isSubmitting}
                    className="px-4 py-2 bg-red-600 hover:bg-red-700 disabled:bg-gray-600 rounded-lg transition"
                    >
                        {isSubmitting ? 'enviando...' : 'enviar reporte'}
                    </button>
                </div>
            </form>
        </div>
    </div>,
    document.body
)}

    </>
)    
}