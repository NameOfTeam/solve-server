FROM openjdk:21-jdk-alpine
WORKDIR /app
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
RUN apk add --no-cache python3
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
