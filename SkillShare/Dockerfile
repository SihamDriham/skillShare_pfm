FROM openjdk:17-jdk-slim

RUN apt-get update && apt-get install -y netcat

ARG JAR_FILE=target/SkillShare-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 3600
