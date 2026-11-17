FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY eureka-server eureka-server/
COPY skill-service skill-service/
COPY notification-service notification-service/
COPY api-gateway api-gateway/

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre

ARG MODULE
WORKDIR /app

COPY --from=builder /app/${MODULE}/target/*.jar app.jar

EXPOSE 8080 8081 8082 8761

ENTRYPOINT ["java", "-jar", "app.jar"]