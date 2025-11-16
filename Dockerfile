# Stage 1: build
FROM maven:3.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -Dmaven.test.skip=true

# Stage 2: runtime
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Default port fallback (Render sẽ ghi đè PORT bằng biến môi trường của nó)
ENV PORT=8080

# Copy any produced jar (robust against artifactId/name)
COPY --from=build /app/target/*.jar app.jar

# Optional but helpful
EXPOSE 8080

# Start app, force Spring Boot to use PORT env
ENTRYPOINT ["sh", "-c", "exec java -Dserver.port=${PORT} -jar app.jar"]