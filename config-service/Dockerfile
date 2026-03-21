FROM eclipse-temurin:25-alpine

RUN apk --no-cache add curl

COPY ./target/config-service-1.0-SNAPSHOT.jar config-service.jar
CMD ["java","-jar","config-service.jar"]