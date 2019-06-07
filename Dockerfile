FROM maven:3.6.1-jdk-12 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /build/src/
RUN mvn package


FROM openjdk:12 as Target
COPY --from=builder /build/target/auth-1.0.0.jar auth.jar

ENV MONGO_URL=pad-b-auth-database \ 
    EUREKA_URL=pad-b-registry \ 
    SERVER_URL=pad-b-auth \ 
    MONGO_DB=admin \ 
    MONGO_USER=admin \ 
    MONGO_PASSWORD=123456 \ 
    DOCKERIZE_VERSION=v0.6.0 \ 
    MONGO_PORT=27017 \ 
    CONFIG_URL=pad-b-config \ 
    CONFIG_PORT=8086

CMD wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \ 
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \ 
    && dockerize -wait tcp://$EUREKA_URL:8761 -timeout 60m yarn start \ 
    && dockerize -wait tcp://$MONGO_URL:$MONGO_PORT -timeout 60m yarn start \
    && dockerize -wait tcp://$CONFIG_URL:$CONFIG_PORT -timeout 60m yarn start

ENTRYPOINT ["java","-jar","/auth.jar"]

EXPOSE 8082