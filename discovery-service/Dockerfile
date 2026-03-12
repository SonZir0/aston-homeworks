FROM eclipse-temurin:25-alpine

RUN apk --no-cache add curl

COPY ./target/discovery-service-1.0-SNAPSHOT.jar discovery-service.jar
CMD ["java","-jar","discovery-service.jar"]