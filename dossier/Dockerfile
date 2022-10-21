FROM openjdk:11
WORKDIR /app
COPY target/dossier.jar /app/

ARG DEAL_CLIENT_URL
ARG KAFKA_SERVER
ARG KAFKA_GROUP_ID

ENTRYPOINT ["java", "-jar", "dossier.jar"]