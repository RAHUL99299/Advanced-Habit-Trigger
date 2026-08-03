# Stage 1: Build Java 17 Spring Boot app using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY habit-backend/pom.xml .
COPY habit-backend/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run environment
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xmx384m", "-Xms128m", "-jar", "app.jar"]
