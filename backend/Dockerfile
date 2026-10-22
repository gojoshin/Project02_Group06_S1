# Stage 1: Build JAR
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN if [ -f "./gradlew" ]; then \
        chmod +x gradlew && ./gradlew bootJar --no-daemon -x test; \
    else \
        cd backend && chmod +x gradlew && ./gradlew bootJar --no-daemon -x test && mkdir -p /app/build/libs && cp build/libs/*.jar /app/build/libs/; \
    fi

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
