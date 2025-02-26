FROM openjdk:21-jdk
WORKDIR /app
COPY target/rest-0.0.1-SNAPSHOT.jar rest.jar
ENTRYPOINT ["java","-jar","rest.jar"]