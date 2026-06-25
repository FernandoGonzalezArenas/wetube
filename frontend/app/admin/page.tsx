'use client'
import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { adminApi } from "@/api/admin";
import VideoCard from "@/components/VideoCard";
import { Check, Trash2, ShieldAlert, Video as VideoIcon, Users, RefreshCw } from "lucide-react";
import { ReportDetail, ReportType } from "@/types/admin";
import Image from "next/image";
import Link from "next/link";

type FilterType = 'VIDEO' | 'USER';

export default function AdminDashboard () {
    const queryClient = useQueryClient();
    const [activeFilter, setActiveFilter] = useState<FilterType>('VIDEO');

 //cargar reportes pendientes
 const {data: reports, isLoading, isError, refetch, isFetching } = useQuery({
queryKey: ['pending-reports'],
queryFn: () => adminApi.getPendingReports(),
 });

 //mutacion para descartar reporte
 const dismissMutation = useMutation({
    mutationFn: ({targetId, type}: {targetId: number, type: ReportType}) => adminApi.dismissReport(targetId, type),
    onSuccess: () => queryClient.invalidateQueries({queryKey: ['pending-reports']}),
 });

 //mutacion para borrar video
 const deleteVideoMutation = useMutation({
    mutationFn: ({ id, reason}: { id: number, reason: string, videoUserId: number}) => adminApi.deleteVideo(id, { reason }),
    onSuccess: (data, variables) => {
const videoId = variables.id;
const authorId = variables.videoUserId;

        queryClient.invalidateQueries({queryKey: ['pending-reports']});

        // 2. Remueve las interacciones de la caché (evita datos fantasma)
        queryClient.invalidateQueries({ queryKey: ['interactions-meta', videoId] });
        queryClient.invalidateQueries({ queryKey: ['interactions-comments', videoId] });

        // 3. Actualiza el perfil del creador (para que limpie su lista infinita y reste el contador de videos)
        if (authorId) {
            queryClient.invalidateQueries({ queryKey: ['user-videos', authorId] });
            queryClient.invalidateQueries({ queryKey: ['user', authorId] }); // Para actualizar el total de videos en la bio
        }
    }
 });

//mutacion para banear usuario
const banUserMutation = useMutation({
    mutationFn: ({id, reason}: {id: number, reason: string}) => adminApi.banUser(id, { reason }),
    onSuccess: (data, variables) => {
        const bannedUserId = variables.id;
        
        queryClient.invalidateQueries({queryKey: ['pending-reports']});

        //invalidar por completo los datos y videos de el usuario especifico
        queryClient.invalidateQueries({queryKey: ['user', bannedUserId]});
        queryClient.invalidateQueries({queryKey: ['user-videos', bannedUserId]});
        queryClient.invalidateQueries({queryKey: ['sub-status', bannedUserId]});

//invalidar de forma global las interacciones de el usuario baneado
queryClient.invalidateQueries({queryKey: ['interactions-meta']});
queryClient.invalidateQueries({queryKey: ['interactions-comments']});
queryClient.invalidateQueries({queryKey: ['sub-status']});
    }
});

const handleAction = (type: 'DISMISS' | 'DELETE_VIDEO' | 'BAN_USER', targetId: number, reportType: ReportType) => {
const reason = prompt("ingresa el motivo de auditoria para  esta accion: ");
if(reason === null) return; //accion cancelada por el admin

if (type === 'DISMISS') {
    dismissMutation.mutate({targetId, type: reportType});
} else if (type === 'DELETE_VIDEO') {
    if(!reason) return alert("el motivo es obligatorio para borrar el video");
const currentReport = reports?.find((r: ReportDetail) => r.targetId === targetId && r.type === 'VIDEO');
const videoUserId = currentReport?.videoUserId || 0;

    deleteVideoMutation.mutate({id: targetId, reason, videoUserId});
} else if (type === 'BAN_USER') {
    if(!reason) return alert("el motivo es obligatorio para banear la cuenta");
    banUserMutation.mutate({id: targetId, reason});
}
}

const filteredReports = reports?.filter((report: ReportDetail) => {
    return report.type=== activeFilter;
}) || [];

if(isLoading) return <div className="p-10 text-white text-center">cargando cola de moderacion... </div>;
if(isError) return <div className="p-10 text-red-500 text-center">error al validar credenciales o cargar reportes... </div>;

return (
    <main className="min-h-screen bg-black text-white p-6 md:p-8 pt-24">
<div className="max-w-7xl mx-auto">

 {/* hencabezado */}
 <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-gray-800 pb-6 mb-8">
<div>
<h1 className="text-3xl font-black -tracking-tight flex items-center gap-3">
    <ShieldAlert className="text-red-500" size={32} />
    panel de administracion
    </h1>
    <p className="text-gray-400 text-sm mt-1">revicion de infracciones reportadas por los usuarios</p>    
</div>
<button
onClick={() => refetch()}
disabled={isFetching}
className="flex items-center justify-center gap-2 bg-gray-900 border border-gray-800 px-4 py-2 rounded-full text-xs font-semibold hover:bg-gray-800 transition disabled:opacity-50 self-start sm:self-center"
>
    <RefreshCw size={14} className={isFetching ? "animate-spin" : ""} />
    actualizar cola
</button>
 </div>

{/* filtros de pestañas (tabs) */}
<div className="flex border-b border-gray-800 gap-2 mb-8 text-sm font-semibold">
    <button
    onClick={() => setActiveFilter('VIDEO')}
    className={`flex items-center gap-2 pb-3 px-4 transition-colors relative ${activeFilter === 'VIDEO' ? 'text-white border-b-2 border-red-500' : 'text-gray-400 hover:text-white'}`}
    >
        <VideoIcon size={16} />
        videos ({reports?.filter((r: ReportDetail) => r.type=== 'VIDEO').length || 0})
    </button>
    <button
    onClick={() => setActiveFilter('USER')}
    className={`flex items-center gap-2 pb-3 px-4 transition-colors relative ${activeFilter === 'USER' ? 'text-white border-b-2 border-red-500' : 'text-gray-400 hover:text-white'}`}
    >
    <Users size={16} />
    cuentas ({reports?.filter((r: ReportDetail) => r.type=== 'USER').length || 0})
    </button>
</div>

{/* grid o lista de reportes */}
{filteredReports.length === 0 ? (
    <div className="text-center py-24 text-gray-500 border border-dashed border-gray-800 rounded-2xl bg-gray-900/10">
no se encontraron reportes pendientes en esta categoría
</div>
) : activeFilter === 'VIDEO' ? (

    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-x-6 gap-y-12">
{filteredReports.map((report: ReportDetail) => {
  //crear el objeto video para la VideoCard
  const mockVideo = {
    id: report.targetId,
    userId: report.videoUserId || 0,
    title: report.videoTitle || "video sin titulo",
    description: report.videoDescription || "",
    duration: report.videoDuration || 0,
    videoUrl: report.videoUrl || "",
    thumbnailUrl: report.thumbnailUrl || "",
    createdAt: report.videoCreatedAt || "",
  };

  return (
    <div key={report.targetId} className="flex flex-col bg-gray-900/40 border border-gray-800 rounded-2xl p-4 hover:border-gray-700 transition gap-4 justify-between">
 <div>       
<VideoCard key={mockVideo.id} video={mockVideo} />

<div className="mt-4 p-3 bg-red-950/20 border border-red-900/30 rounded-xl text-xs">
<div className="flex justify-between items-center mb-2">
<span className="text-red-400 font-bold">reportes resibidos: {report.totalReports}</span>
</div>

{/* lista de motivos con cantidad de reportes de cada motivo en este recurso */}
<div className="flex flex-wrap gap-1.5 mb-2">
{Object.entries(report.reasonsCount || {}).map(([reasonKey, count]) => (
    <span key={reasonKey} className="px-2 py-0.5 bg-black/40 border border-red-900/50 rounded text-gray-300 font-mono">
        {reasonKey}: <strong className="text-white">{count}</strong>
    </span>
))}
</div>

{/* descripciones de los reportes desplegadas */}
<div className="max-h-20 overflow-y-auto text-gray-400 italic bg-black/20 p-2 rounded border border-gray-850 font-sans">
{report.reportDescriptions.map((desc) => (
    <p key={desc.reportId} className="mb-1 border-b border-gray-900 last:border-0 pb-0.5 text-[11px]">
• <span className="text-gray-500 font-mono text-[9px]">#{desc.reportId}:</span> {desc.description || "sin comentarios"}
    </p>
))}
</div>

</div>
</div>

{/* botonera de acciones */}
<div className="grid grid-cols-2 gap-2 mt-2 pt-3 border-t border-gray-800/60">
<button
onClick={() => handleAction('DISMISS', report.targetId, report.type)}
className="flex items-center justify-center gap-1.5 py-2 bg-gray-800 hover:bg-gray-700 text-green-400 text-xs font-bold rounded-lg transition"
>
    <Check size={14} />
    Descartar
</button>
<button
onClick={() => handleAction('DELETE_VIDEO', report.targetId, report.type)}
className="flex items-center justify-center gap-1.5 py-2 bg-red-600 hover:bg-red-700 text-white text-xs font-bold rounded-lg transition"
>
    <Trash2 size={14} />
    Eliminar
</button>
</div>
</div>
  )
})}
</div>
) : (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
{filteredReports.map((report: ReportDetail) => (
 <div key={report.targetId} className="bg-gray-900 border border-gray-800 rounded-2xl p-5 flex flex-col justify-between gap-5 hover:border-gray-700 transition">
<Link href={`/profile/${report.targetId}`} className="flex items-center gap-4">

{/* foto de perfil o avatar del usuario reportado */}
<div className="relative w-14 h-14 shrink-0 rounded-full overflow-hidden bg-gray-800 border-2 border-gray-700 flex items-center justify-center text-xl font-black text-white">
    {report.type=== 'USER' && report.profilePictureUrl && report.targetUsername ? (
<Image
src={report.profilePictureUrl}
alt={report.targetUsername}
fill
className="object-cover"
sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
/>
    ) : (
<Users size={24} className="text-gray-400" />
    )}
</div>
<p className="text-sm font-medium text-gray-300 group-hover:text-white transition-colors">{report.targetUsername}</p>
</Link>

<div className="mt-4 p-3 bg-gray-950/20 border border-gray-900/30 rounded-xl text-xs">
<div className="flex justify-between items-center mb-2">
<span className="text-red-400 font-bold">reportes resibidos {report.totalReports}</span>
</div>

<div className="flex flex-wrap gap-1.5 mb-2">
{Object.entries(report.reasonsCount || {}).map(([reasonKey, count]) => (
    <span key={reasonKey} className="px-2 py-0.5 bg-black/40 border border-red-900/50 rounded text-gray-300 font-mono">
        {reasonKey}: <strong className="text-white">{count}</strong>
    </span>
))}
</div>

<div className="max-h-20 overflow-y-auto text-gray-400 italic bg-black/20 p-2 rounded border border-gray-850 font-sans">
{report.reportDescriptions.map((desc) => (
    <p key={desc.reportId} className="mb-1 border-b border-gray-900 last:border-0 pb-0.5 text-[11px]">
        • <span className="text-gray-500 font-mono text-[9px]">#{desc.reportId}:</span> {desc.description || "sin comentarios"}
    </p>
))}
</div>
</div>

{/* acciones para cuentas */}
<div className="flex justify-end gap-2 border-t border-gray-800/60 pt-4">
<button
onClick={() => handleAction('DISMISS', report.targetId, report.type)}
className="flex items-center gap-1.5 px-4 py-2 bg-gray-800 hover:bg-gray-700 text-green-400 text-xs font-bold rounded-lg transition"
>
    <Check size={14} />
    mantener cuenta
</button>

<button
onClick={() => handleAction('BAN_USER', report.targetId, report.type)}
className="flex items-center gap-1.5 px-4 py-2 bg-red-600/20 hover:bg-red-600 text-red-400 hover:text-white text-xs font-bold rounded-lg transition border border-red-900/40"
>
    <ShieldAlert size={14} />
    banear usuario
</button>
</div>
</div>
    ))}
</div>
)}
</div>
    </main>
)

}