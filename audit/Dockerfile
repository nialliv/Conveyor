FROM openjdk:11
WORKDIR /app
COPY target/audit.jar /app/
ENTRYPOINT ["java", "-jar", "audit.jar"]