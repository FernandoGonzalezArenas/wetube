#!/bin/sh

# Esperar a que MinIO esté listo
sleep 5;

# Configurar el acceso
mc alias set myminio http://minio:9000 ${MINIO_ROOT_USER} ${MINIO_ROOT_PASSWORD};

# Definir la política CORS en un archivo temporal
echo '{
  "CORSRules": [
    {
      "AllowedOrigins": ["*"],
      "AllowedMethods": ["PUT", "GET", "POST", "DELETE", "HEAD"],
      "AllowedHeaders": ["*"],
      "ExposeHeaders": ["ETag"]
    }
  ]
}' > /tmp/cors.json;

# Aplicar CORS solo a los buckets específicos
# El comando 'mc ls' verifica si existe, pero 'mc cors set' simplemente falla si no está.
# Aquí lo aplicamos a ambos buckets:
mc cors set myminio/teakter-videos /tmp/cors.json || echo "Bucket videos no encontrado aún."
mc cors set myminio/teakter-users /tmp/cors.json || echo "Bucket users no encontrado aún."

echo "Configuración de CORS completada correctamente.";
exit 0;