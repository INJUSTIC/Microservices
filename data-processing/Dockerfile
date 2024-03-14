FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/data-processing-1.0-SNAPSHOT.jar data-processing.jar
ENTRYPOINT ["java", "-jar", "data-processing.jar"]