FROM openjdk:12
ADD ./target/auth-1.0.0.jar auth.jar
ENV MONGO_URL=pad-b-auth-database
ENV EUREKA_URL=pad-b-registry
ENV MONGO_DB=auth
ENV MONGO_USER=admin
ENV MONGO_PASSWORD=123456
ENTRYPOINT ["java","-jar","/auth.jar"]

EXPOSE 8082