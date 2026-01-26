FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/comments-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]