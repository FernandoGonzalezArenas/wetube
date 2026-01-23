FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/likes-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]