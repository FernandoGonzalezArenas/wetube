'use client'
import {QueryClient, QueryClientProvider } from '@tanstack/react-query';
import {useState } from 'react';
import "./globals.css";
import dynamic from 'next/dynamic';
import { AuthProvider} from "@/hooks/useAuth";

//importamos el navbar desactivando el Server Side Rendering
const Navbar = dynamic(() => import('@/components/navbar'), { ssr: false });


export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
const [queryClient] =useState(() => new QueryClient());

  return (
    <html
      lang="es"
    >
      <body className="min-h-full flex flex-col">
        <QueryClientProvider client={queryClient}>

          <AuthProvider>
       <Navbar/>   
        {children}
</AuthProvider>

</QueryClientProvider>        
</body>
    </html>
  );
}
