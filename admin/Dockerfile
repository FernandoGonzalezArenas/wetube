FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/admin-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]