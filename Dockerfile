FROM gradle:jdk21 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /home/gradle/src/build/libs/Auto-Repair-Shop-Project-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]