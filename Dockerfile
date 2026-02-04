# Stage 1: Build using Maven and Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Stage 2: Run using a slim JRE 21 image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Optimization for 100M DAU: Enable Virtual Threads and memory tuning
ENTRYPOINT ["java", "-XX:+UseZGC", "-Xmx2g", "-jar", "app.jar"]