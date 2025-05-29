FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /alignify-backend
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=build /target/*.jar alignify-backend-1.0.0.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar" ,"alignify-backend-1.0.0.jar"]