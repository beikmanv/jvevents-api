FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY build/libs/jvevents-api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
