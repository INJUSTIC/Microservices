FROM maven:3.9.3-eclipse-temurin-17-alpine@sha256:1cbc71cb8e2f594338f4b4cbca897b9f9ed6183e361489f1f7db770d57efe839 AS builder
WORKDIR /build
COPY pom.xml app/
COPY data-generation/src app/data-generation/src
COPY data-generation/pom.xml app/data-generation/
WORKDIR /build/app/data-generation
RUN mvn package -DskipTests




