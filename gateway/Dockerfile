FROM openjdk:11
WORKDIR /app
COPY target/gateway.jar /app/

ARG APPLICATION_CLIENT_URL
ARG DEAL_CLIENT_URL

ENTRYPOINT ["java", "-jar", "gateway.jar"]