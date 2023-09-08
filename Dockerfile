#docker build --platform linux/amd64 -t docker-test .

#base image: openjdk:17-alpine
FROM openjdk:17-oracle

WORKDIR /app

#cp spring-boot-web.jar /opt/app/app.jar
ARG JAR_FILE=build/libs/triplog-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

#java -jar -Dspring.profiles.active=prod app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]


