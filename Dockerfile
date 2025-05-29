# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /alignify-backend
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven && mvn clean package -DskipTests

# Package stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /alignify-backend
COPY --from=build /alignify-backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]