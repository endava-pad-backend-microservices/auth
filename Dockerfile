FROM maven:3.6.1-jdk-12 AS builder
COPY ./ .
RUN mvn clean package

FROM openjdk:12 as Target
COPY --from=builder target/auth-1.0.0.jar auth.jar
ENV MONGO_URL=pad-b-auth-database
ENV EUREKA_URL=pad-b-registry
ENV SERVER_URL=pad-b-auth
ENV MONGO_DB=admin
ENV MONGO_USER=admin
ENV MONGO_PASSWORD=123456


ENV DOCKERIZE_VERSION v0.6.0

CMD wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz && dockerize -wait tcp://$EUREKA_URL:8761 -timeout 60m yarn start && dockerize -wait tcp://$MONGO_URL:27017 -timeout 60m yarn start


ENTRYPOINT ["java","-jar","/auth.jar"]

EXPOSE 8082