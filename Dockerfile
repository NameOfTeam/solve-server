FROM openjdk:21
COPY --from=python:3.11 / /
WORKDIR /app
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
