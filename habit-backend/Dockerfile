# Stage 1: Build Java 17 Spring Boot app using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/habit-backend-1.0.0.jar app.jar
EXPOSE 8080 10000
ENTRYPOINT ["sh", "-c", "java -Xmx384m -Xms128m -Dserver.port=${PORT:-8080} -jar app.jar"]
