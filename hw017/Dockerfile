FROM openjdk:11-jre-slim
COPY /target/docker-compose-example.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


# В качестве базового образа практически всегда имеет смысл использовать какой-нибудь образ от Liberica, потому
# что они просто меньше.

# https://github.com/petrelevich/otus_java_2022_06/blob/main/L09-docker/jib.gradle

# image = 'bellsoft/liberica-openjdk-alpine-musl:17.0.2-9'