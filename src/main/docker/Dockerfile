FROM openjdk:17-jdk
ARG JAR_FILE=*.jar
COPY init-db.sql /docker-entrypoint-initdb.d/
COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080