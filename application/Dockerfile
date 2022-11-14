FROM openjdk:11
WORKDIR /app
COPY target/application.jar /app/
ARG SERVER_PORT
ARG DEAL_CLIENT_URL
ENTRYPOINT ["java", "-jar", "application.jar"]