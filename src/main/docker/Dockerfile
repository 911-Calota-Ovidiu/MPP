FROM openjdk:17-jdk
ARG JAR_FILE=*.jar
COPY init-db.sql /docker-entrypoint-initdb.d/
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]
