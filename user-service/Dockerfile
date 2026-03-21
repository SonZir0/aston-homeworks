FROM eclipse-temurin:25-alpine

RUN apk --no-cache add curl

COPY ./target/user-service-1.0-SNAPSHOT.jar user-service.jar
CMD ["java","-jar","user-service.jar"]