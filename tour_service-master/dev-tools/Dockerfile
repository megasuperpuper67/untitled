FROM maven:3.9.16-eclipse-temurin-25-alpine AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

COPY src/main/resources/application.yml /app/application.yml

ENTRYPOINT ["java", "-jar", "app.jar"]