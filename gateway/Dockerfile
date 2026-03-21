FROM eclipse-temurin:25-alpine

RUN apk --no-cache add curl

COPY ./target/gateway-1.0-SNAPSHOT.jar gateway.jar
CMD ["java","-jar","gateway.jar"]