# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar into the container
COPY --from=build /workspace/target/KamalCart-0.0.1-SNAPSHOT.jar app.jar

# Copy production properties file into the container
COPY src/main/resources/application-prod.properties /app/config/application-prod.properties

# Copy the environment file into the container
COPY ./.env /app/.env

# Set Spring config location via environment variable
ENV SPRING_CONFIG_LOCATION=file:/app/config/application-prod.properties
ENV SPRING_PROFILES_ACTIVE=prod

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
