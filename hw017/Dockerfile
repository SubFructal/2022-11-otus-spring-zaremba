FROM bellsoft/liberica-openjdk-alpine:17.0.3.1
COPY /target/hw017-app.jar /app/app-hw017.jar
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "/app/app-hw017.jar"]