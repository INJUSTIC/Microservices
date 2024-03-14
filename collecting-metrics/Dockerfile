FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/collecting-metrics-1.0-SNAPSHOT.jar collecting-metrics.jar
ENTRYPOINT ["java", "-jar", "collecting-metrics.jar"]