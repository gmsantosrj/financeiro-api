FROM openjdk:21-jdk-slim

WORKDIR /app

ARG JAR_FILE

COPY target/${JAR_FILE} /app/api.jar
COPY wait-for-it.sh /wait-for-it.sh

EXPOSE 8080

CMD ["java", "-jar", "api.jar"]