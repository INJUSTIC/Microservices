


FROM maven:3.9.3-eclipse-temurin-17-alpine@sha256:1cbc71cb8e2f594338f4b4cbca897b9f9ed6183e361489f1f7db770d57efe839 AS builder
ARG serviceName

WORKDIR /build
# Copy the Swagger Codegen generated client API project files
COPY swagger-generated-client-api /build/swagger-generated-client-api
# Build the client API
WORKDIR /build/swagger-generated-client-api
RUN mvn package -DskipTests

WORKDIR /build
COPY pom.xml app/
COPY ${serviceName}/src app/${serviceName}/src
COPY ${serviceName}/pom.xml app/${serviceName}/

WORKDIR /build/app/${serviceName}

RUN mvn package -DskipTests

FROM eclipse-temurin:17.0.6_10-jre-alpine@sha256:c26a727c4883eb73d32351be8bacb3e70f390c2c94f078dc493495ed93c60c2f
ARG serviceName

WORKDIR /app
COPY --from=builder /build/app/${serviceName}/target/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

