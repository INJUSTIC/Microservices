FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/data-generation-1.0-SNAPSHOT.jar data-generation.jar
ENTRYPOINT ["java", "-jar", "data-generation.jar"]