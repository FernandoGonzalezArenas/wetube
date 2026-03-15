FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY target/subscription-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]