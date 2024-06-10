FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/code-challenge-0.0.1-SNAPSHOT.jar code-challenge-app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "code-challenge-app.jar"]