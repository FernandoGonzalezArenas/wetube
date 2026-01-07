#usar una imagen base con java 21
FROM eclipse-temurin:21-jdk-jammy
#crear un directorio de trabajo en el contenedor
WORKDIR /app
#copiar el archivo jar de el microservicio previamente creado con mvn clean package 
COPY target/*.jar app.jar

#exponer el puerto que usa el microservicio
EXPOSE 8082
#comando para ejecutar la aplicacion
ENTRYPOINT ["java", "-jar", "app.jar"]