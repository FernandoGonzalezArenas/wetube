#usar imagen base con java 21
FROM eclipse-temurin:21-jdk-jammy

#crear un directorio de trabajo en el contenedor
WORKDIR /app

#copiar el archivo jar previamente creado a el contenedor 
COPY target/*.jar app.jar

#exponer el puerto que usa el microservicio
EXPOSE 8081

#comando para ejecutar la aplicacion
ENTRYPOINT ["java", "-jar", "app.jar"]