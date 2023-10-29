FROM openjdk:17-jdk-slim

WORKDIR /app
COPY build/libs/*SNAPSHOT.jar ptis-batch.jar

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "ptis-batch.jar"]