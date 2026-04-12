/*
componente de la pagina principal
*/

export default function Home() {
  return (
    <main className="p-8">
      <h1 className="text-2xl font-bold">Bienvenido a WeTube</h1>
      <p>El frontend está conectado y funcionando.</p>
      
      <section className="mt-4">
        <button 
          className="bg-red-600 text-white px-4 py-2 rounded"
          aria-label="Ir a videos"
        >
          Explorar Videos
        </button>
      </section>
    </main>
  );
}