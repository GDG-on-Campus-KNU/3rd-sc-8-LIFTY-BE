FROM openjdk:17-jdk-slim

WORKDIR /opt/app

ARG JAR_FILE=build/libs/gdsc.sc8-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","app.jar"]

EXPOSE 8080