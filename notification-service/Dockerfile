FROM eclipse-temurin:25-alpine

RUN apk --no-cache add curl

COPY ./target/notification-service-1.0-SNAPSHOT.jar notification-service.jar
CMD ["java","-jar","notification-service.jar"]